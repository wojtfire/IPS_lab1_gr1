package com.example.demo;

import com.example.demo.database.ClickHouse;
import com.example.demo.services.ClickhouseService;
import com.example.demo.services.DatabaseFactory;
import com.example.demo.services.DatabaseService;
import com.example.demo.services.MysqlService;
import com.example.demo.web.dto.TableEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(DemoApplication.class, args);
	}
}
