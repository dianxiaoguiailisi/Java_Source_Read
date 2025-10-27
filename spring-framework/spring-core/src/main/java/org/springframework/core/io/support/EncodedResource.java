/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core.io.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * 适配器模式 对接InputStreamSource和resource
 * 目标接口：InputStreamSource，这是希望的接口
 * 被适配者：resource 是 Spring 中表示资源的接口，EncodedResource 对其进行了功能增强。
 * 适配器：EncodedResource：实现了目标接口 InputStreamSource，内部持有 Resource 对象（被适配者），
 * 		并通过 getInputStream() 方法直接委托给 Resource 的 getInputStream()（满足目标接口的基础功能）
 * 		额外增加了编码 / 字符集（encoding/charset）的处理逻辑，提供了 getReader() 方法（基于 Resource 的输入流，
 * 		按指定编码创建字符流），本质是对 Resource 的功能进行了适配和增强
 * 1. 解决 “编码处理” 的功能扩展需求：Resource 是 Spring 中底层资源的基础接口，核心职责是提供资源的字节流（InputStream），不关心字符编码。而实际场景中，
 * 	当资源是文本文件时，必须指定编码才能正确读取字符（否则可能出现乱码）。EncodedResource 通过适配器模式，在 Resource 基础上增加了 encoding 和 charset 属性，
 * 	并提供 getReader() 方法 —— 这个方法会基于Resource 的字节流，按指定编码创建 Reader（字符流）。这相当于为 Resource 扩展了 “编码感知” 的能力，
 * 	而无需修改 Resource 接口本身（符合 “开闭原则”）。
 * 	2. 保持与上层接口（InputStreamSource）的兼容性
 *
 */

public class EncodedResource implements InputStreamSource {

	private final Resource resource;//被适配的对象

	@Nullable
	private final String encoding;

	@Nullable
	private final Charset charset;

	public EncodedResource(Resource resource) {
		this(resource, null, null);
	}

	public EncodedResource(Resource resource, @Nullable String encoding) {
		this(resource, encoding, null);
	}


	public EncodedResource(Resource resource, @Nullable Charset charset) {
		this(resource, null, charset);
	}

	private EncodedResource(Resource resource, @Nullable String encoding, @Nullable Charset charset) {
		super();
		Assert.notNull(resource, "Resource must not be null");
		this.resource = resource;
		this.encoding = encoding;
		this.charset = charset;
	}


	public final Resource getResource() {
		return this.resource;
	}

	@Nullable
	public final String getEncoding() {
		return this.encoding;
	}

	@Nullable
	public final Charset getCharset() {
		return this.charset;
	}

//判断是否需要字符流（Reader）
	public boolean requiresReader() {
		return (this.encoding != null || this.charset != null);
	}

//获取带编码的字符流:基于资源的字节流（InputStream），创建带指定编码的字符流（Reader）
	public Reader getReader() throws IOException {
		if (this.charset != null) {//优先使用 Charset（字符集对象）创建 InputStreamReader（字符流与字节流的桥梁）
			return new InputStreamReader(this.resource.getInputStream(), this.charset);
		}
		else if (this.encoding != null) {//若未指定 Charset，则使用 encoding（字符串编码名）创建
			return new InputStreamReader(this.resource.getInputStream(), this.encoding);
		}
		else {
			return new InputStreamReader(this.resource.getInputStream());
		}
	}
	//获取原始字节流（实现 InputStreamSource 接口）
	@Override
	public InputStream getInputStream() throws IOException {
		return this.resource.getInputStream();
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EncodedResource)) {
			return false;
		}
		EncodedResource otherResource = (EncodedResource) other;
		return (this.resource.equals(otherResource.resource) &&
				ObjectUtils.nullSafeEquals(this.charset, otherResource.charset) &&
				ObjectUtils.nullSafeEquals(this.encoding, otherResource.encoding));
	}

	@Override
	public int hashCode() {
		return this.resource.hashCode();
	}

	@Override
	public String toString() {
		return this.resource.toString();
	}

}
