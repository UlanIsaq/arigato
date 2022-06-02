package com.binarycod.arigato.controllers;

import com.binarycod.arigato.domain.Product;
import com.binarycod.arigato.repository.ProductRepository;
import com.binarycod.arigato.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductsController {

    List<Product> productList = new ArrayList<>();

    @Autowired
    ProductService productService;

    String error="";
        @GetMapping
        public String getProducts(Model model){
Integer numberOfProducts = productService.getCount();
            model.addAttribute("products", productService.getProducts());
            //model.addAttribute("product",new Product()); it was for POST method, but when we move it to another  page this model became useless
            model.addAttribute("productCount",numberOfProducts);

            model.addAttribute("error",error);
            return "product_list";
        }
@GetMapping("/new")
public String newProduct(Model model){
           model.addAttribute("product", new Product());
            return "new_product";
}
        @PostMapping
    public String createProduct(Product p){
            System.out.println("I am handling post to  this endpoint");
            System.out.println(p.getId()+" "+p.getName()+" "+p.getPrice());
            productService.createProduct(p);
            return "redirect:/products";
        }

        @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id){
            System.out.println("I will delte"+id);
           productService.deleteProduct(id);
            return "redirect:/products";
        }
          @GetMapping("/edit")
        public String editProduct(@RequestParam Long id, Model model){

              Optional<Product> productOptional = productService.getProduct(id);
              if(!productOptional.isPresent())
              {
                  error = "No such product in out store!";
                  return "redirect:/products";
              }

             model.addAttribute("product", productOptional.get());

            return "edit_product";
        }

        @PostMapping("/edit")
    public String savePorduct(Product product)
        {
            Optional <Product> productOld = productList
                    .stream()
                    .filter(p -> p.getId().equals(product.getId()))
                    .findFirst();
            if(productOld.isPresent())
            {
                productList.remove(productOld.get());
                productList.add(product);
            }
            /*for(Product p: productList){
                if(p.getId().equals(product.getId()))
                {
                    p.setName(product.getName());
                    p.setPrice(product.getPrice());

                }
            }*/
            return"redirect:/products";
        }
int num=0;
        @GetMapping("/discount")
    public String applyDiscount(@RequestParam String dis)
    {

        if(!productList.isEmpty()) {
            if (dis.equalsIgnoreCase("discount1")&&num==0) {
                for (Product p : productList) {
                    p.setPrice(p.getPrice() * 0.7);
                    num++;
                    System.out.println(num);

                }
            }
        }
        return "redirect:/products";
    }
    @GetMapping("/disc")
    public String applDiscount(@RequestParam Long id, @RequestParam String did)
    {

       /* for (Product p:productList) {
            if(p.getId()==id && num==0){
                p.setPrice(p.getPrice()*0.7);
                num++;
            }
        }*/

        for (Product p:productList) {

            if(p.getId()==id&&did.equalsIgnoreCase("discount") && p.getDiscount()==0){
                p.setPrice(p.getPrice()*0.7);
                num = p.getDiscount();
                num++;
                p.setDiscount(num);
            }
        }
        return "redirect:/products";
    }

}
