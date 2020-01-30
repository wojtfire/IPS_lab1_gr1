export enum DatabaseEnum {
  MYSQL = "MYSQL",
  CLICKHOUSE = "CLICKHOUSE",
  BOTH = "BOTH"
}


export const getDabaseEnum = (value: string): DatabaseEnum => {
  switch (value) {
    case DatabaseEnum.MYSQL:
      return DatabaseEnum.MYSQL;
    case DatabaseEnum.CLICKHOUSE:
      return DatabaseEnum.CLICKHOUSE;
    default:
      return DatabaseEnum.BOTH;
  }
};
