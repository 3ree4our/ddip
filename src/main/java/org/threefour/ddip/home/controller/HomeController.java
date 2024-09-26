package org.threefour.ddip.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.RepresentativeImagesRequest;
import org.threefour.ddip.image.service.ImageService;
import org.threefour.ddip.product.domain.GetProductsResponse;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.service.ProductService;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.PageableGenerator;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.threefour.ddip.util.PaginationConstant.*;
import static org.threefour.ddip.util.SessionConstant.MEMBER_ID;
import static org.threefour.ddip.util.SessionConstant.PRODUCTS;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;
    private final ImageService imageService;

    @GetMapping("/")
    public ModelAndView index(
            @RequestParam(defaultValue = TRUE) String paged,
            @RequestParam(defaultValue = ZERO) String pageNumber,
            @RequestParam(defaultValue = NINE) String size,
            @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING) String sort,
            @RequestParam(defaultValue = ZERO) String categoryId,
            HttpSession httpSession
    ) {
        Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);
        Page<Product> products
                = productService.getProducts(pageable, FormatConverter.parseToShort(categoryId));
        List<Image> representativeImages
                = imageService.getRepresentativeImages(RepresentativeImagesRequest.from(products.getContent()));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject(PRODUCTS, GetProductsResponse.from(products, representativeImages));
        modelAndView.addObject(MEMBER_ID, httpSession.getAttribute(MEMBER_ID));

        return modelAndView;
    }
}
