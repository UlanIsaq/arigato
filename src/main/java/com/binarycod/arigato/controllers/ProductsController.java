package com.binarycod.arigato.controllers;

import com.binarycod.arigato.domain.Product;
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


        @GetMapping
        public String getProducts(Model model){

            model.addAttribute("products", productList);

            return "product_list";
        }

        @PostMapping
    public String createProduct(@RequestParam Long pid, @RequestParam String pname, @RequestParam Double price){
            System.out.println("I am handling post to  this endpoint");
            System.out.println(pid+" "+pname+" "+price);
            productList.add(new Product(pid,pname,price));
            return "redirect:/products";
        }

        @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id){
            System.out.println("I will delte"+id);
            productList = productList
                    .stream()
                    .filter(p -> !p.getId().equals(id))
                    .collect(Collectors.toList());
            return "redirect:/products";
        }
          @GetMapping("/edit")
        public String editProduct(@RequestParam Long id, Model model){
              Optional<Product> productOptional = productList
                      .stream()
                      .filter(p -> p.getId().equals(id))
                      .findFirst();
              if(!productOptional.isPresent())
              {
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


}
