package com.example.diplomenproekt.web;

import com.example.diplomenproekt.models.bindingModel.AddCompanyBindingModel;
import com.example.diplomenproekt.services.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final ModelMapper modelMapper;
    private final HttpSession httpSession;

    public CompanyController(CompanyService companyService, ModelMapper modelMapper, HttpSession httpSession) {
        this.companyService = companyService;
        this.modelMapper = modelMapper;
        this.httpSession = httpSession;
    }

    @GetMapping("/add-company")
    public String addCompany(Model model){

        if (httpSession.getAttribute("email") == null) {
            return "redirect:/";
        }

        if (!model.containsAttribute("email")) {
            model.addAttribute("email", httpSession.getAttribute("email"));
        }

        return "add-company";
    }

    @PostMapping("/add-company")
    public String addCompanyConfirm(@Valid AddCompanyBindingModel addCompanyBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Throwable {

        if (httpSession.getAttribute("email") == null) {
            return "redirect:/";
        }

         if(bindingResult.hasErrors()){
             redirectAttributes.addFlashAttribute("addCompanyBindingModel", addCompanyBindingModel);
             redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addCompanyBindingModel",bindingResult);

             return "redirect:/companies/add-company";
         }

        this.companyService.addCompany(addCompanyBindingModel, httpSession.getAttribute("email"));

        return "redirect:/";
    }

    @ModelAttribute
    AddCompanyBindingModel addCompanyBindingModel(){
        return new AddCompanyBindingModel();
    }

}
