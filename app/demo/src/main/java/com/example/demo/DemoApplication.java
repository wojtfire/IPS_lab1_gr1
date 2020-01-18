package com.example.demo;

import com.example.demo.services.ClickhouseService;
import com.example.demo.services.DatabaseFactory;
import com.example.demo.services.DatabaseService;
import com.example.demo.services.MysqlService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(DemoApplication.class, args);

		ClickhouseService ch = new ClickhouseService();
		MysqlService mysql = new MysqlService();

		List<DatabaseService> services = new ArrayList<DatabaseService>();
		services.add(ch);
		services.add(mysql);

		DatabaseFactory factory = new DatabaseFactory();
//		factory.initializeDatabase(Database.BOTH);
//		factory.loadAllTablesData(services);
	}
}
