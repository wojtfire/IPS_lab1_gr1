import { Pipe, PipeTransform } from "@angular/core";
import { DatabaseEnum, getDabaseEnum } from "../enum/database-enum";
import { TableEnum } from "../enum/table-enum";

@Pipe({ name: "filterTables", pure: false })
export class FilterTablesPipe implements PipeTransform {
  transform(tables: string[], database: DatabaseEnum): string[] {
    if (tables.length === 0) {
      return [];
    }

    tables.filter(t => {
      return t.split(":").map(val => val.trim())[0] === database;
    });
    return tables.filter(
      t => t.split(":").map(val => val.trim())[0] === database
    );
  }
}
