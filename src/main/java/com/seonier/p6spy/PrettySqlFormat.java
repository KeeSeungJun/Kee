package com.seonier.p6spy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrettySqlFormat implements MessageFormattingStrategy {

	@Override
	public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared,
			String sql, String url) {
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return format.format(currentDate) + " | " + "OperationTime: " + elapsed + " ms " + formatSql(sql);
	}

	private String formatSql(String sql) {
		if (StringUtils.isBlank(sql)) {
			return sql;
		}

		try (BufferedReader br = new BufferedReader(new StringReader(sql))) {
			sql = br.lines().filter(cs -> StringUtils.isNotBlank(cs)).map(line -> line + "\n")
					.collect(Collectors.joining());
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return sql;
	}
}
