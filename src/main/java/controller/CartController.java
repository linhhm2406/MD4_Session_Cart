package controller;

import model.Product;
import model.ProductInCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import service.IProductService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("/")
//public class CartController {
//
//    @Autowired
//    HttpSession httpSession;
//
//    @Autowired
//    IProductService productService;
//
//    @GetMapping()
//    public ModelAndView showList() {
//        return new ModelAndView("showList", "list", productService.findAll());
//    }
//
//    @GetMapping("/productDetail/{id}")
//    public ModelAndView showDetail(@PathVariable Long id) {
//        Product product = productService.findById(id);
//        return new ModelAndView("productDetail", "product", product);
//    }
//
//    @PostMapping("/addToCart")
//    public String addToCart(@ModelAttribute Product product) {
//        Product pickedUpProduct = productService.findById(product.getId());
//        Object list = httpSession.getAttribute("productListInCart");
//        List<ProductInCart> productList = new ArrayList<>();
//        if (list == null) {
//            ProductInCart productInCart = new ProductInCart(pickedUpProduct.getId(), pickedUpProduct.getName(), pickedUpProduct.getPrice(), 1);
//            productList.add(productInCart);
//        } else {
//            productList = (List<ProductInCart>) list;
//            ProductInCart productFoundInList = null;
//            for (int i = 0; i < productList.size(); i++) {
//                if (product.getId().equals(productList.get(i).getId())) {
//                    productFoundInList = productList.get(i);
//                }
//            }
//            if (productFoundInList != null) {
//                productFoundInList.setQuantity(productFoundInList.getQuantity() + 1);
//            } else {
//                ProductInCart productInCart = new ProductInCart(pickedUpProduct.getId(), pickedUpProduct.getName(), pickedUpProduct.getPrice(), 1);
//                productList.add(productInCart);
//            }
//        }
//        httpSession.setAttribute("productListInCart", productList);
//        List<ProductInCart> list2 = (List<ProductInCart>) httpSession.getAttribute("productListInCart");
//        return "redirect:/";
//    }
//}

@SessionAttributes("productListInCart")
@RequestMapping("/")
public class CartController {

    @ModelAttribute ("productListInCart")
    List<ProductInCart> productListInCart(){
        return new ArrayList<>();
    }

    @Autowired
    IProductService productService;

    @GetMapping()
    public ModelAndView showList() {
        return new ModelAndView("showList", "list", productService.findAll());
    }

    @GetMapping("/productDetail/{id}")
    public ModelAndView showDetail(@PathVariable Long id) {
        Product product = productService.findById(id);
        return new ModelAndView("productDetail", "product", product);
    }

    @PostMapping("/addToCart")
    public String addToCart(@ModelAttribute Product product, @ModelAttribute("productListInCart") List<ProductInCart> list) {
        Product pickedUpProduct = productService.findById(product.getId());
            ProductInCart productFoundInList = getProductFindOutInCart(product, list);
            if (productFoundInList != null) {
                increaseQuantityProduct(productFoundInList);
            } else {
                ProductInCart productInCart = makeNewProductAddToCart(pickedUpProduct);
                list.add(productInCart);
            }
        return "redirect:/";
    }

    private ProductInCart makeNewProductAddToCart(Product pickedUpProduct) {
        return new ProductInCart(pickedUpProduct.getId(), pickedUpProduct.getName(), pickedUpProduct.getPrice(), 1);
    }

    private void increaseQuantityProduct(ProductInCart productFoundInList) {
        productFoundInList.setQuantity(productFoundInList.getQuantity() + 1);
    }

    private ProductInCart getProductFindOutInCart(@ModelAttribute Product product, @ModelAttribute("productListInCart") List<ProductInCart> list) {
        ProductInCart productFoundInList = null;
        for (int i = 0; i < list.size(); i++) {
            if (product.getId().equals(list.get(i).getId())) {
                productFoundInList = list.get(i);
            }
        }
        return productFoundInList;
    }

    @GetMapping("/showCart")
    ModelAndView showCart(@ModelAttribute("productListInCart") List<ProductInCart> list){
        float total =0;
        for (ProductInCart pro : list) {
            total = total+(pro.getQuantity()*pro.getPrice());
        }
        ModelAndView modelAndView = new ModelAndView("showCart");
        modelAndView.addObject("listInCart",list);
        modelAndView.addObject("total",total);
        return modelAndView;
    }

    @PostMapping("/update")
    String update(@RequestParam String quantity, @ModelAttribute("productListInCart") List<ProductInCart> list){
        String[] quantityList = quantity.split(",");
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setQuantity(Integer.parseInt(quantityList[i]));
        }
    return "redirect:/showCart";
    }
}
