import {
  Component,
  OnInit,
  OnDestroy,
  AfterViewChecked,
  Query
} from "@angular/core";
import { FormGroup, FormBuilder } from "@angular/forms";
import { AppService } from "./app-service.service";
import { Subject, Observable, Observer } from "rxjs";
import { DatabaseEnum, getDabaseEnum } from "./enum/database-enum";
import { takeUntil } from "rxjs/operators";
import { DatabaseTablePipe } from "./pipe/database-table.pipe";
import {
  TruncateDto,
  DatabaseDataDto,
  DatabaseTablesDto,
  QueryDto
} from "./dto/api-models";
import { MatSnackBar } from "@angular/material/snack-bar";
import { TableEnum, getTableEnum } from "./enum/table-enum";
import { FilterTablesPipe } from "./pipe/filter-tables.pipe";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent implements OnInit, OnDestroy, AfterViewChecked {
  DatabaseEnum = DatabaseEnum;
  TableEnum = TableEnum;
  form: FormGroup;
  databaseTables: string[] = [];
  tablesToTruncate: string[];
  sqlQuery: string;
  chosenDatabaseToQuery: DatabaseEnum;
  loading: boolean;
  availableTables = Object.keys(TableEnum);
  benchmarks: QueryDto[] = [];

  destroy$ = new Subject<boolean>();
  tableLoaded$ = new Subject<boolean>();
  tableLoadFinished$ = new Subject<boolean>();
  tableTruncate$ = new Subject<boolean>();
  tableTruncateFinished$ = new Subject<boolean>();

  constructor(
    private service: AppService,
    private databaseTablePipe: DatabaseTablePipe,
    private snackBar: MatSnackBar,
    private filterPipe: FilterTablesPipe
  ) {}

  ngOnInit() {
    this.getAvailableTables();
    // this.form = this.fb.group({

    // })
  }

  ngOnDestroy(): void {
    this.destroy$.next();
  }

  ngAfterViewChecked(): void {
    // console.log(this.benchmarks);
  }

  truncateTables() {
    if (!this.tablesToTruncate.length) {
      return;
    }
    this.loading = true;
    const table = this.tablesToTruncate.shift();
    const dto = this.prepareDatabaseDto(table);
    this.service
      .truncateTable(dto)
      .pipe(takeUntil(this.tableTruncateFinished$ || this.destroy$))
      .subscribe(
        benchmark => {
          this.openSnackBar(
            `Table ${dto.tableName.toLowerCase()} truncated`,
            "OK"
          );
          this.addNewBenchmark(
            dto.databaseName,
            `Table ${dto.tableName.toLowerCase()} truncate`,
            benchmark.elapsedTime,
            benchmark.rows
          );
          if (this.tablesToTruncate.length) {
            this.truncateTables();
          } else {
            this.openSnackBar(`All tables truncated`, "OK");
            this.loading = false;
            this.tablesToTruncate = [];
          }
        },
        err => {
          this.loading = false;
          this.openSnackBar("Some error ocured", "OK");
          this.tablesToTruncate = [];
        }
      );
  }

  private getAvailableTables() {
    this.loading = true;
    this.service
      .getTables()
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        dto => {
          this.setDatabaseTables(dto);
          this.loading = false;
        },
        err => (this.loading = false)
      );
  }

  private parseTablesToTruncate(tables: string[]): TruncateDto {
    const dto = { databaseDataList: [] } as TruncateDto;
    tables &&
      tables.forEach(table => {
        dto.databaseDataList.push(this.prepareDatabaseDto(table));
      });
    return dto;
  }

  private setDatabaseTables(dto: DatabaseTablesDto) {
    if (!dto) {
      return;
    }
    this.databaseTables.length = 0;
    if (dto.clickhouseTables) {
      this.databaseTables.push(
        ...this.databaseTablePipe.transform(
          dto.clickhouseTables,
          DatabaseEnum.CLICKHOUSE
        )
      );
    }
    if (dto.mysqlTables) {
      this.databaseTables.push(
        ...this.databaseTablePipe.transform(dto.mysqlTables, DatabaseEnum.MYSQL)
      );
    }
  }

  private prepareDatabaseDto(value: string): DatabaseDataDto {
    const dto = {} as DatabaseDataDto;
    const arr = value.split(":").map(val => val.trim());
    dto.databaseName = getDabaseEnum(arr[0]);
    dto.tableName = getTableEnum(arr[1]);
    return dto;
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000
    });
  }

  initDb(dbName: DatabaseEnum) {
    this.loading = true;
    this.service
      .initDb(dbName)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        res => {
          res.statusText === "OK" &&
            this.openSnackBar("Database initialized", "OK");
          this.loading = false;
          this.getAvailableTables();
        },
        err => (this.loading = false)
      );
  }

  loadAllTablesData(database: DatabaseEnum) {
    this.loading = true;
    const tableName = this.availableTables.shift();
    const dto = {} as DatabaseDataDto;
    dto.tableName = tableName;
    dto.databaseName = database;
    this.service
      .loadTableData(dto)
      .pipe(takeUntil(this.tableLoadFinished$ || this.destroy$))
      .subscribe(
        res => {
          this.openSnackBar(
            `Table ${tableName} loaded. Loading took: ${res.elapsedTime}seconds`,
            "OK"
          );
          this.addNewBenchmark(
            dto.databaseName,
            `Data load to ${dto.tableName.toLowerCase()} table`,
            res.elapsedTime,
            res.rows
          );
          if (this.availableTables.length) {
            this.loadAllTablesData(database);
          } else {
            this.openSnackBar(`All ${database} tables loaded`, "OK");
            this.finishTableLoad();
          }
        },
        err => {
          this.finishTableLoad();
          this.openSnackBar("Some error ocured", "OK");
        }
      );
  }

  executeQuery() {
    if (!this.sqlQuery || !this.chosenDatabaseToQuery) {
      return;
    }
    this.loading = true;
    const dto = {} as DatabaseDataDto;
    dto.databaseName = this.chosenDatabaseToQuery;
    dto.query = this.sqlQuery;
    this.service
      .executeQuery(dto)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        benchmark => {
          this.openSnackBar(`Query took ${benchmark.elapsedTime}`, "OK");
          this.addNewBenchmark(
            dto.databaseName,
            dto.query,
            benchmark.elapsedTime,
            benchmark.rows
          );
          this.loading = false;
          this.getAvailableTables();
        },
        err => {
          this.loading = false;
          this.openSnackBar("Error ocured", "OK");
        }
      );
  }

  private finishTableLoad() {
    this.loading = false;
    this.availableTables = Object.keys(TableEnum);
    this.tableLoadFinished$.next();
  }

  private addNewBenchmark(
    databaseName: DatabaseEnum,
    query: string,
    elapsedTime: string,
    rows: number
  ) {
    const dto = {} as QueryDto;
    dto.databaseName = databaseName;
    dto.query = query;
    dto.elapsedTime = elapsedTime;
    dto.rows = rows;
    this.benchmarks.push(dto);
  }
}
