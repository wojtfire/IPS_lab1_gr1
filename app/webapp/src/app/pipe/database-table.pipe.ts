import { Pipe, PipeTransform } from "@angular/core";
import { DatabaseEnum } from "../enum/database-enum";
import { TableEnum } from "../enum/table-enum";

@Pipe({ name: "databaseTable" })
export class DatabaseTablePipe implements PipeTransform {
  transform(tables: string[], database: DatabaseEnum): string[] {
    if (!tables || !tables.length) {
      return [];
    }
    return tables.map(table => {
      if (database === DatabaseEnum.CLICKHOUSE) {
        return DatabaseEnum.CLICKHOUSE + " : " + table.toUpperCase();
      } else {
        return DatabaseEnum.MYSQL + " : " + table.toUpperCase();
      }
    });
  }
}
