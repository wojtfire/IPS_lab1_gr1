import { Pipe, PipeTransform } from "@angular/core";
import { DatabaseEnum } from "../enum/database-enum";
import { TableEnum } from "../enum/table-enum";

@Pipe({ name: "databaseTable" })
export class DatabaseTablePipe implements PipeTransform {
  transform(tables: TableEnum[], database: DatabaseEnum): string[] {
    return tables.map(table => {
      if (database === DatabaseEnum.CLICKHOUSE) {
        return DatabaseEnum.CLICKHOUSE + " : " + table;
      } else {
        return DatabaseEnum.MYSQL + " : " + table;
      }
    });
  }
}
