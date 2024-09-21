package org.threefour.ddip.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.threefour.ddip.deal.exception.DealsAlreadyExistException;
import org.threefour.ddip.deal.service.DealService;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.RepresentativeImagesRequest;
import org.threefour.ddip.image.service.ImageService;
import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.GetCategoriesResponse;
import org.threefour.ddip.product.category.service.CategoryService;
import org.threefour.ddip.product.domain.*;
import org.threefour.ddip.product.service.ProductService;
import org.threefour.ddip.util.AuthenticationValidator;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;
import org.threefour.ddip.util.PageableGenerator;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.threefour.ddip.deal.exception.ExceptionMessage.DEALS_ALREADY_EXIST_EXCEPTION_MESSAGE;
import static org.threefour.ddip.image.domain.TargetType.PRODUCT;
import static org.threefour.ddip.util.PaginationConstant.*;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final DealService dealService;

    @GetMapping("/registration-form")
    public ModelAndView getRegistrationForm(String memberId, HttpSession httpSession) {
        if (!AuthenticationValidator.isAuthorized(memberId, httpSession)) {
            return new ModelAndView("redirect:/member/login");
        }

        System.out.println(memberId + "dsafkjl;sdj;fkl");
        System.out.println(httpSession.getAttribute("memberId").toString());
        List<Category> categories = categoryService.getCategories(null);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("product/registration");
        modelAndView.addObject("memberId", memberId);
        modelAndView.addObject("categories", GetCategoriesResponse.from(categories));

        return modelAndView;
    }

    @PostMapping("/registration-confirm")
    public String registerProduct(
            @ModelAttribute RegisterProductRequest registerProductRequest,
            @RequestParam("images") @RequestPart List<MultipartFile> images,
            HttpSession httpSession
    ) {
        System.out.println(registerProductRequest.getMemberId() + "asdl;fjsdalkf");
        System.out.println(httpSession.getAttribute("memberId").toString());

        if (!AuthenticationValidator.isAuthorized(registerProductRequest.getMemberId(), httpSession)) {
            return "redirect:/member/login";
        }

        Long productId = productService.createProduct(registerProductRequest, images);
        return String.format("redirect:details?id=%d", productId);
    }

    @GetMapping("/list")
    public ModelAndView getProducts(
            @RequestParam(defaultValue = TRUE) String paged,
            @RequestParam(defaultValue = MINUS_ONE) String pageNumber,
            @RequestParam(defaultValue = NINE) String size,
            @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING) String sort,
            @RequestParam(defaultValue = ZERO) String categoryId,
            HttpSession httpSession
    ) {
        if (pageNumber.equals(MINUS_ONE)) {
            httpSession.setAttribute("categoryId", categoryId);
            pageNumber = ZERO;
        }
        if (categoryId.equals(ZERO)) {
            categoryId = (String) httpSession.getAttribute("categoryId");
        }
        if (!FormatValidator.hasValue(categoryId)) {
            categoryId = ZERO;
        }

        Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);
        Page<Product> products
                = productService.getProducts(pageable, FormatConverter.parseToShort(categoryId));
        List<Image> representativeImages
                = imageService.getRepresentativeImages(RepresentativeImagesRequest.from(products.getContent()));

        return new ModelAndView("product/list", "products", GetProductsResponse.from(products, representativeImages));
    }

    @GetMapping("/details")
    public ModelAndView getProduct(@RequestParam String id, HttpSession httpSession) {
        if (!FormatValidator.hasValue(id) || !FormatValidator.isPositiveNumberPattern(id)) {
            return new ModelAndView("redirect:list");
        }
        Long parsedId = FormatConverter.parseToLong(id);
        Object memberId = httpSession.getAttribute("memberId");
        int waitingNumber = -1;
        if (FormatValidator.hasValue(memberId)) {
            waitingNumber = dealService.getWaitingNumber(parsedId, (Long) memberId);
        }

        return new ModelAndView(
                "product/details", "product",
                GetProductResponse.from(
                        productService.getProduct(parsedId, true),
                        imageService.getImages(PRODUCT, parsedId),
                        waitingNumber
                )
        );
    }

    @GetMapping("/modification-form")
    public ModelAndView getModificationForm(
            @RequestParam String id, @RequestParam String memberId, HttpSession httpSession
    ) {
        if (!FormatValidator.hasValue(id) || !FormatValidator.isPositiveNumberPattern(id)) {
            return new ModelAndView("redirect:list");
        }
        if (!AuthenticationValidator.isAuthorized(memberId, httpSession)) {
            return new ModelAndView("redirect:/member/login");
        }

        ModelAndView modelAndView = new ModelAndView();
        Long parsedId = FormatConverter.parseToLong(id);
        modelAndView.addObject(
                "product",
                GetProductResponse.from(
                        productService.getProduct(parsedId, false), imageService.getImages(PRODUCT, parsedId)
                )
        );
        modelAndView.addObject("categories", GetCategoriesResponse.from(categoryService.getCategories(null)));
        modelAndView.addObject("memberId", memberId);
        modelAndView.setViewName("product/modification");

        return modelAndView;
    }

    @PatchMapping("/update")
    public ResponseEntity<Long> updateAttribute(
            @RequestBody UpdateProductRequest updateProductRequest, HttpSession httpSession
    ) {
        productService.update(updateProductRequest);

        return ResponseEntity.status(OK).body((Long) httpSession.getAttribute("memberId"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProduct(@RequestParam("id") String id) {
        Long parsedId = FormatConverter.parseToLong(id);

        if (dealService.getWaitingNumberCount(parsedId) > 0) {
            throw new DealsAlreadyExistException(String.format(DEALS_ALREADY_EXIST_EXCEPTION_MESSAGE, id));
        }
        productService.delete(parsedId);

        return ResponseEntity.status(NO_CONTENT).build();
    }
}
