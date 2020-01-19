import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { DatabaseEnum } from "./enum/database-enum";
import { ROUTES } from "./routing/routing";
import { Observable } from "rxjs";
import {
  DatabaseTablesDto,
  TruncateDto,
  DatabaseDataDto,
  BenchmarkDto
} from "./dto/api-models";
import { TableEnum } from "./enum/table-enum";

const URL = "http://localhost:8090/api";
const headers = new HttpHeaders().set("Content-Type", "application/json");

@Injectable({
  providedIn: "root"
})
export class AppService {
  constructor(private http: HttpClient) {}

  public truncateTables(dto: TruncateDto) {
    return this.http.post<DatabaseTablesDto>(URL + ROUTES.TRUNCATE, dto, {
      observe: "response"
    });
  }

  public getTables(): Observable<DatabaseTablesDto> {
    return this.http.get<DatabaseTablesDto>(URL + ROUTES.TABLES);
  }

  public initDb(dbName: DatabaseEnum) {
    return this.http.post(URL + ROUTES.DATABASE, dbName.toString(), {
      observe: "response"
    });
  }

  public loadTableData(dto: DatabaseDataDto): Observable<BenchmarkDto> {
    return this.http.post<BenchmarkDto>(URL + ROUTES.LOAD, dto);
  }

  public getAllTableData(dto: DatabaseDataDto): Observable<BenchmarkDto> {
    return this.http.post<BenchmarkDto>(URL + ROUTES.TABLEDATA, dto);
  }

  public executeQuery(dto: DatabaseDataDto): Observable<BenchmarkDto> {
    return this.http.post<BenchmarkDto>(URL + ROUTES.QUERY, dto);
  }
}
