package org.threefour.ddip.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponseDTO {
  private Long productId;
  private String title;
  private Long sellerId;
  private String image;
}
