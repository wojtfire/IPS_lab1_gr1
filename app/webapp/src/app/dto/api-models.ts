import { DatabaseEnum } from "../enum/database-enum";

export interface DatabaseDataDto {
  databaseName?: DatabaseEnum;
  tableName?: string;
  query?: string;
}

export interface TruncateDto {
  databaseDataList?: DatabaseDataDto[];
}

export interface DatabaseTablesDto {
  mysqlTables?: string[];
  clickhouseTables?: string[];
}

export interface BenchmarkDto {
  elapsedTime?: string;
  rows?: number;
}
