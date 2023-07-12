package demo.pafday28demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import demo.pafday28demo.model.Pokemon;
import demo.pafday28demo.model.PokemonType;
import demo.pafday28demo.service.PokemonService;

@Controller
@RequestMapping
public class MainController {
    
@Autowired
private PokemonService pSvc;

    @GetMapping
    public String landingPage(Model model) {
        List<String> typeList = pSvc.getType();

        model.addAttribute("typeList", typeList);
        return "home";
    }

    @GetMapping(path="/search") 
    public String searchResult(@RequestParam String name, Model model) {
        try {
            List<Document> pokemonDoc = pSvc.findPokemonByName(name);
            Document d = pokemonDoc.get(0);
            Pokemon pokemon = pSvc.docToPokemon(d);

            model.addAttribute("pokemon", pokemon);
            return "pokemon";

        } catch (NoSuchElementException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
       
    }

    @GetMapping(path="/type")
    public String getType(Model model) {
        List<Document> pokemonType = pSvc.findPokemonType();
        List<PokemonType> typeList = new ArrayList<>();
        
        for(Document doc : pokemonType) {
            PokemonType type = pSvc.docToType(doc);

            typeList.add(type);
        }

        model.addAttribute("type", typeList);
        
        return "type";
    }

    @GetMapping(path="/pokemon/{type}")
    public String pokemonByType(@PathVariable String type, Model model) {
        List<Document> pokemon = pSvc.findPokemonByType(type);
        List<Pokemon> result = new ArrayList<>();

        for(Document d : pokemon) {
            Pokemon p = pSvc.docToPokemon(d);
            result.add(p);
        }

        model.addAttribute("result", result);
        return "result";
    }

}
