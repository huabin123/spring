package com.my;

import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * @Author huabin
 * @DateTime 2023-05-19 16:32
 * @Desc
 */
public class MyConvert implements Converter<List<String>, TestBean> {
	@Override
	public TestBean convert(List<String> source) {
		return new TestBean();
	}
}
