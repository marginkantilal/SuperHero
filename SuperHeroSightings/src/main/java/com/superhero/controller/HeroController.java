package com.superhero.controller;

import com.superhero.model.Organisation;
import com.superhero.model.Power;
import com.superhero.model.Hero;
import com.superhero.service.OrganizationService;
import com.superhero.service.PowerService;
import com.superhero.service.HeroService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HeroController {
    
   private final OrganizationService organizationService;
   private final PowerService powerService;
   private final HeroService heroService;  
   
   public HeroController(OrganizationService organizationService, PowerService powerService, HeroService superService) {
        this.organizationService = organizationService;
        this.powerService = powerService;    
        this.heroService = superService;    
    }
    
   
   @GetMapping("heroes") //Go to hero html page
    public String displaySupers(Model model) {
        List<Hero> heroes = heroService.getAllSupers();
        List<Power> powers = powerService.getAllPowers();
        List<Organisation> organizations = organizationService.getAllOrganizations();
        model.addAttribute("supers", heroes);
        model.addAttribute("powers", powers);
        model.addAttribute("organizations", organizations);
        model.addAttribute("super", model.getAttribute("super") != null ? model.getAttribute("super") : new Hero());
        
        return "heroes"; //returning "supers" means we will need a supers.html file to push our data to
    }
    
    @PostMapping("addheroes")
    public String addSuper(@Valid Hero supper, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes redirect) throws IOException {  
        List<String> powerIds = Arrays.asList(Optional.ofNullable(request.getParameterValues("power_id")).orElse(new String[0])); //if the received parameter is null, create an empty list
        List<String> organizationIds = Arrays.asList(Optional.ofNullable(request.getParameterValues("organization_id")).orElse(new String[0]));
        
        if(result.hasErrors()){  
            List<Hero> supers = heroService.getAllSupers();
            model.addAttribute("supers", supers); //to fill the listing
            return displaySupers(model);
        }            
        
        heroService.addSuper(supper, powerIds, organizationIds);        
        return "redirect:/heroes";
    }
    
    @GetMapping("detailSuper") //Go to detailSuper html page
    public String superDetail(Integer id, Model model) {
        Hero supper = heroService.getSuperById(id);
        model.addAttribute("super", supper);
        return "detailSuper";
    }
    
    @GetMapping("displayDeleteSuper") //Go to deleteSuper html page for confirmation
    public String displayDeleteSuper(Integer id, Model model) { 
        Hero supper = heroService.getSuperById(id);
        model.addAttribute("super", supper);
        return "deleteSuper";
    }
    
    @GetMapping("deleteSuper")
    public String deleteSuper(Integer id) {
        heroService.deleteSuperById(id);
        return "redirect:/supers";
    }
    
    @GetMapping("editSuper") //Go to editSuper html page
    public String editSuper(Integer id, Model model) {
        Hero supper = heroService.getSuperById(id);
        List<Power> powers = powerService.getAllPowers();
        List<Organisation> organizations = organizationService.getAllOrganizations();
        
        if(model.getAttribute("super") != null) {
            ((Hero) model.getAttribute("super")).setPowers(supper.getPowers());
            ((Hero) model.getAttribute("super")).setOrganizations(supper.getOrganizations());
        }
        model.addAttribute("super", model.getAttribute("super") != null ? model.getAttribute("super") : supper);
        model.addAttribute("powers", powers);
        model.addAttribute("organizations", organizations);
        return "editSuper";
    }
    
    @PostMapping("editSuper")
    public String performEditSuper(@Valid Hero supper, BindingResult result, HttpServletRequest request, Model model) {        
        List<String> powerIds = Arrays.asList(Optional.ofNullable(request.getParameterValues("power_id")).orElse(new String[0]));
        List<String> organizationIds = Arrays.asList(Optional.ofNullable(request.getParameterValues("organization_id")).orElse(new String[0]));
        
        if(result.hasErrors()){  
            return editSuper(supper.getId(), model);
        } 
        
        heroService.updateSuper(supper, powerIds, organizationIds);

        return "redirect:/detailSuper?id=" + supper.getId();
    }
    
   
    
}
