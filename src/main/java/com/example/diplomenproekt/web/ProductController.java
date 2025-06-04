package com.example.diplomenproekt.web;

import com.example.diplomenproekt.models.bindingModel.AddToCartBindingModel;
import com.example.diplomenproekt.models.bindingModel.CheckoutBindingModel;
import com.example.diplomenproekt.models.entities.enums.CategoryEnum;
import com.example.diplomenproekt.services.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductsService productsService;
    private final ModelMapper modelMapper;
    private final HttpSession httpSession;
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final HistoryService historyService;

    public ProductController(ProductsService productsService, ModelMapper modelMapper, HttpSession httpSession,  ShoppingCartService shoppingCartService, UserService userService, HistoryService historyService) {
        this.productsService = productsService;
        this.modelMapper = modelMapper;
        this.httpSession = httpSession;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.historyService = historyService;
    }





    @GetMapping("/catalog/{category}")
    public String catalog(Model model, @PathVariable String category) {

        if (httpSession.getAttribute("email") == null) {
            return "redirect:/";
        }

        if (!model.containsAttribute("email")) {
            model.addAttribute("email", httpSession.getAttribute("email"));
        }

        String[] allowed_currencies = {
                "BTC",
                "ETH",
                "USDT",
                "XRP",
                "BNB",
                "SOL",
                "USDC",
                "DOGE",
                "TRX",
                "ADA",
                "HYPE",
                "SUI",
                "LINK",
                "AVAX",
                "XLM",
                "BCH",
                "LEO",
                "TON",
                "SHIB",
                "HBAR"
        };

        if (category.equals("all"))
        {
            model.addAttribute("products", productsService.getAllProducts());
        }
        else if (Arrays.stream(allowed_currencies).anyMatch(s -> s.equals(category)))
        {
            model.addAttribute("products", productsService.getAllOf(CategoryEnum.valueOf(category)));
        }
        else {
            // TODO: 404 please
        }

        model.addAttribute("chosen_currency", category);

        return "products-catalog";
    }

    @GetMapping("/{id}/details")
    public String details(@PathVariable Long id, Model model) throws Throwable {

        if (httpSession.getAttribute("email") == null) {
            return "redirect:/";
        }

        if (!model.containsAttribute("email")) {
            model.addAttribute("email", httpSession.getAttribute("email"));
        }

        if (!model.containsAttribute("canBuy")) {
            model.addAttribute("canBuy", true);
        }

        model.addAttribute("product", productsService.findProductById(id));

        return "details";
    }

    @PostMapping("/{id}/details")
    public String confirmDetails(@PathVariable Long id, AddToCartBindingModel addToCartBindingModel, RedirectAttributes redirectAttributes) throws Throwable {

        boolean canBuy = productsService.checkQuantityAvailability(addToCartBindingModel.getPortion(), id);

        if (!canBuy || addToCartBindingModel.getPortion() <= 0) {

            redirectAttributes.addFlashAttribute("addToCartBindingModel", addToCartBindingModel);
            redirectAttributes.addFlashAttribute("canBuy", false);

            return "redirect:/products/" + id + "/details";
        }

        productsService.addProductToCart(addToCartBindingModel.getPortion(), id, httpSession.getAttribute("email"));

        return "redirect:/products/shopping-cart";
    }

    @GetMapping("/shopping-cart")
    public String checkout(Model model) throws Throwable {

        if (httpSession.getAttribute("email") == null) {
            return "redirect:/";
        }

        if (!model.containsAttribute("email")) {
            model.addAttribute("email", httpSession.getAttribute("email"));
        }

        model.addAttribute("products", shoppingCartService.findAllShoppingCartEntities());
        model.addAttribute("productsCount", shoppingCartService.findAllShoppingCartEntities().size());
        model.addAttribute("totalPrice", userService.findProductsCartTotalPrice(httpSession.getAttribute("email")));
        model.addAttribute("totalPriceDelivery", userService.findProductsCartTotalPrice(httpSession.getAttribute("email")) + 5);

        return "shopping-cart";
    }

    @PostMapping("/shopping-cart")
    public String checkoutConfirm(@Valid CheckoutBindingModel checkoutBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Throwable {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("checkoutBindingModel", checkoutBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.checkoutBindingModel", bindingResult);

            return "redirect:/products/shopping-cart";
        }

        historyService.secureCheckout(checkoutBindingModel.getAddress(), httpSession.getAttribute("email"), checkoutBindingModel.getTotalPrice());

        return "redirect:/products/order-details";
    }

    @GetMapping("/order-details")
    public String orderDetails(Model model) {

        if (httpSession.getAttribute("email") == null) {
            return "redirect:/";
        }

        if (!model.containsAttribute("email")) {
            model.addAttribute("email", httpSession.getAttribute("email"));
        }

        return "delivery";
    }

    @GetMapping("/my-history")
    public String history(Model model) throws Throwable {

        if (httpSession.getAttribute("email") == null) {
            return "redirect:/";
        }

        if (!model.containsAttribute("email")) {
            model.addAttribute("email", httpSession.getAttribute("email"));
        }

        model.addAttribute("historyProducts", historyService.findAllHistoryProducts(httpSession.getAttribute("email")));

        return "history";
    }

    @PostMapping("/my-history")
    public String clearHistory() throws Throwable {
        historyService.clearHistory(httpSession.getAttribute("email"));
        return "redirect:/products/my-history";
    }

    @ModelAttribute
    public CheckoutBindingModel checkoutBindingModel() {
        return new CheckoutBindingModel();
    }



    @ModelAttribute
    public AddToCartBindingModel addToCartBindingModel() {
        return new AddToCartBindingModel();
    }
}
