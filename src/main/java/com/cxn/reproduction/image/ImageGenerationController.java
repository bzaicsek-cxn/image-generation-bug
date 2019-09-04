/*
 * Copyright 2019 ChemAxon Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cxn.reproduction.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/generate")
@RestController
@Validated
public class ImageGenerationController {
	
	private static final int WIDTH_HEIGHT = 100;

	@GetMapping(produces=MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] genrateImage(
				@RequestParam(name="text",  defaultValue="test") @NotBlank         String text,
				@RequestParam(name="red",   defaultValue="255")  @Min(0) @Max(255) int    red,
				@RequestParam(name="green", defaultValue="160")  @Min(0) @Max(255) int    green,
				@RequestParam(name="blue",  defaultValue="0")    @Min(0) @Max(255) int    blue
			) throws Exception {
		BufferedImage image = new BufferedImage(WIDTH_HEIGHT, WIDTH_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		Color textColor = new Color(red, green, blue);
		paintBackground(g, textColor);
		paintText(text, textColor, g);
		return exportImage(image);
	}

	@ExceptionHandler(ConstraintViolationException.class)	
	public ResponseEntity<String> handleValidationException(ConstraintViolationException exception) {
		return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(exception.getMessage());
	}
	
	private byte[] exportImage(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		baos.flush();
		baos.close();
		return baos.toByteArray();
	}

	private void paintText(String text, Color textColor, Graphics g) {
		g.setColor(textColor);
		g.drawString(text, 20, 20);
	}
	
	private void paintBackground(Graphics g, Color textColor) {
		Color bgColor = textColor.getRed() + textColor.getGreen() + textColor.getBlue() > 3 * 255 / 2 ? Color.BLACK
				: Color.WHITE;
		g.setColor(bgColor);
		g.fillRect(0, 0, WIDTH_HEIGHT, WIDTH_HEIGHT);
	}
}
