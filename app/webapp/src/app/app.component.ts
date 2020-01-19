import { Component, OnInit, OnDestroy, AfterViewChecked } from "@angular/core";
import { FormGroup, FormBuilder } from "@angular/forms";
import { AppService } from "./app-service.service";
import { Subject, Observable, Observer } from "rxjs";
import { DatabaseEnum, getDabaseEnum } from "./enum/database-enum";
import { takeUntil } from "rxjs/operators";
import { DatabaseTablePipe } from "./pipe/database-table.pipe";
import {
  TruncateDto,
  DatabaseDataDto,
  DatabaseTablesDto
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

  destroy$ = new Subject<boolean>();
  tableLoaded$ = new Subject<boolean>();
  tableLoadFinished$ = new Subject<boolean>();

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
    // console.log(
    //   this.filterPipe.transform(this.databaseTables, DatabaseEnum.CLICKHOUSE)
    // );
  }

  truncateTables() {
    if (!this.tablesToTruncate.length) {
      return;
    }
    this.loading = true;
    this.service
      .truncateTables(this.parseTablesToTruncate(this.tablesToTruncate))
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        res => {
          if (res.statusText === "OK") {
            this.openSnackBar("Tables truncated", "OK");
            this.tablesToTruncate = [];
            this.loading = false;
          }
        },
        err => (this.loading = false)
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

  parseTablesToTruncate(tables: string[]): TruncateDto {
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
      .pipe(takeUntil(this.tableLoadFinished$))
      .subscribe(
        res => {
          this.openSnackBar(
            `Table ${tableName} loaded. Loading took: ${res.elapsedTime}seconds`,
            "OK"
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
}
