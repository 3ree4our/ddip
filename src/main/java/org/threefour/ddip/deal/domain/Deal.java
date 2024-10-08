package org.threefour.ddip.deal.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.threefour.ddip.audit.BaseGeneralEntity;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.product.domain.Product;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@ToString
public class Deal extends BaseGeneralEntity {
  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "seller_id")
  private Member seller;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "buyer_id")
  private Member buyer;

  @Convert(converter = DealAmount.DealAmountConverter.class)
  @Column(nullable = false)
  private DealAmount dealAmount;

  @Enumerated(EnumType.STRING)
  private DealStatus dealStatus;

  @Column(nullable = false)
  private int waitingNumber;

  @Builder
  private Deal(
          Product product, Member seller, Member buyer,
          DealAmount dealAmount, DealStatus dealStatus, int waitingNumber
  ) {
    this.product = product;
    this.seller = seller;
    this.buyer = buyer;
    this.dealAmount = dealAmount;
    this.dealStatus = dealStatus;
    this.waitingNumber = waitingNumber;
  }

  public static Deal from(
          InitializeDealRequest initializeDealRequest, Product product, Member seller, Member buyer, int waitingNumber
  ) {
    return Deal.builder()
            .product(product)
            .seller(seller)
            .buyer(buyer)
            .dealAmount(DealAmount.of(initializeDealRequest.getDealAmount()))
            .dealStatus(DealStatus.BEFORE_DEAL)
            .waitingNumber(waitingNumber)
            .build();
  }

  public Product getProduct() {
    return product;
  }

  public Member getSeller() {
    return seller;
  }

  public Member getBuyer() {
    return buyer;
  }

  public DealAmount getDealAmount() {
    return dealAmount;
  }

  public DealStatus getDealStatus() {
    return dealStatus;
  }

  public int getWaitingNumber() {
    return waitingNumber;
  }

  public void setDealStatus(DealStatus dealStatus) {
    this.dealStatus = dealStatus;
  }
}
