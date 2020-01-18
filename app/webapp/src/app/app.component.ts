import { Component, OnInit, OnDestroy, AfterViewChecked } from "@angular/core";
import { FormGroup, FormBuilder } from "@angular/forms";
import { AppService } from "./app-service.service";
import { Subject } from "rxjs";
import { DatabaseEnum, getDabaseEnum } from "./enum/database-enum";
import { takeUntil } from "rxjs/operators";
import { DatabaseTablePipe } from "./pipe/database-table.pipe";
import { TruncateDto, DatabaseDataDto } from "./dto/api-models";
import { MatSnackBar } from "@angular/material/snack-bar";
import { TableEnum, getTableEnum } from "./enum/table-enum";

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

  destroy$ = new Subject<boolean>();
  tableLoaded$ = new Subject<boolean>();

  constructor(
    private service: AppService,
    private databaseTablePipe: DatabaseTablePipe,
    private snackBar: MatSnackBar
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
    // console.log(this.chosenDatabaseToQuery);
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
            this.openSnackBar("Tables truncated", null);
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
        tables => {
          this.setDatabaseTables(tables);
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

  private setDatabaseTables(tables) {
    this.databaseTables = [
      ...this.databaseTablePipe.transform(
        tables.mysqlTables,
        DatabaseEnum.MYSQL
      ),
      ...this.databaseTablePipe.transform(
        tables.clickhouseTables,
        DatabaseEnum.CLICKHOUSE
      )
    ];
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
            this.openSnackBar("Database initialized", null);
          this.loading = false;
        },
        err => (this.loading = false)
      );
  }

  loadAllTablesData(database: DatabaseEnum) {
    this.loading = true;
    Object.keys(TableEnum).forEach((tableName, index) => {
      const dto = {} as DatabaseDataDto;
      dto.tableName = tableName;
      dto.databaseName = database;
      this.service
        .loadTableData(dto)
        .pipe(takeUntil(this.tableLoaded$))
        .subscribe(
          benchmark => {
            console.log("ok");
            this.openSnackBar(`Table ${tableName} loaded`, null);
            if (index === 5) {
              this.loading = false;
            }
            this.tableLoaded$.next();
          },
          err => (this.loading = false)
        );
    });
  }
}
