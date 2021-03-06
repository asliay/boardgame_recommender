
package com.example.finalproject.server.components;

import com.example.finalproject.server.client.Client;
import com.example.finalproject.server.models.BoardGame;
import com.example.finalproject.server.models.Credential;
import com.example.finalproject.server.models.User;
import com.example.finalproject.server.repositories.BoardGameRepository;
import com.example.finalproject.server.repositories.CredentialRepository;
import com.example.finalproject.server.repositories.UserRepository;
import com.example.finalproject.server.models.Category;
import com.example.finalproject.server.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    BoardGameRepository boardGameRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CredentialRepository credentialRepository;


    @Autowired
    Client client;

    public DataLoader() {
    }

    public void run(ApplicationArguments args) throws IOException {

        Credential bobsLogIn = new Credential("badams@bobslawblog.com", "rabbitf4n");
        User bobAdams = new User( "Bob", "Adams", "1985-10-20");
        userRepository.save(bobAdams);
        bobAdams.setCredential(bobsLogIn);
        bobsLogIn.setUser(bobAdams);
        userRepository.save(bobAdams);

        // Following is client loading data from BGA API.

        // MAX = 100 - Variable for how many games to return.
        int numOfGames = 100;

        // Populate the categories table.
        ArrayList<Category> categoriesFromBGA = client.getAllCatergories();
        for(Category cat : categoriesFromBGA){
            categoryRepository.save(cat);
        }

        // Getting map of BoardGames with a value list of their BGA category IDs.
        HashMap<BoardGame, List<String>> bgWithCategories = client.getBoardGamesFromBGA(numOfGames);

        // Creates a set of boardgame objects from the above.
        Set<BoardGame> keys = bgWithCategories.keySet();

        // Get our saved  categories as objects from the db.
        List<Category> dbCategories = categoryRepository.findAll();

        // Adding relative categories to boardgames, then saves to db.
        for(BoardGame bg : keys){
            for(Category cat : dbCategories){
                for(String str : bgWithCategories.get(bg)){
                    // substring is removing "" that appear in the string from bgWithCategories cat list.
                    if(str.substring(1, str.length() -1).equals(cat.getBgaId())){
                        bg.addCategoryToGame(cat);
                    }
                }
            }
            boardGameRepository.save(bg);
        }

    }

}


//    Category family = new Category("7rV11PKqME", "Family");
//        categoryRepository.save(family);
//
//                Category economic = new Category("N0TkEGfEsF", "Economic");
//                categoryRepository.save(economic);
//
//                BoardGame catan          = new BoardGame("Settlers of Catan", 1995, 3, 4, 120,
//                "https://cf.geekdo-images.com/W3Bsga_uLP9kO91gZ7H8yw__thumb/img/8a9HeqFydO7Uun_le9bXWPnidcA=/fit-in/200x150/filters:strip_icc()/pic2419375.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/81eKRDSc-PL._AC_SL1500_.jpg", 39, "seed placeholder");
//                catan.addCategoryToGame(economic);
//                catan.addCategoryToGame(family);
//                boardGameRepository.save(catan);
//
//
//                BoardGame coup           = new BoardGame("Coup", 2012, 2, 6, 15,
//                "https://cf.geekdo-images.com/MWhSY_GOe2-bmlQ2rntSVg__thumb/img/vuR_0PCX1w2EkjO_LbchOHZPOwU=/fit-in/200x150/filters:strip_icc()/pic2016054.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/51cnlYAh-6L._AC_.jpg", 38, "seed placeholder");
//                boardGameRepository.save(coup);
//
//
//                BoardGame gloomhaven     = new BoardGame("Gloomhaven", 2017, 1, 4, 120,
//                "https://cf.geekdo-images.com/sZYp_3BTDGjh2unaZfZmuA__thumb/img/veqFeP4d_3zNhFc3GNBkV95rBEQ=/fit-in/200x150/filters:strip_icc()/pic2437871.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/81crhhZd63L._AC_SL1500_.jpg", 37, "seed placeholder");
//                boardGameRepository.save(gloomhaven);
//
//                BoardGame codenames      = new BoardGame("Codenames", 2015, 2, 8, 15,
//                "https://cf.geekdo-images.com/F_KDEu0GjdClml8N7c8Imw__thumb/img/yl8iXxSNwguMeg3KkmfFO9SMVVc=/fit-in/200x150/filters:strip_icc()/pic2582929.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/81UWfQ1DwwL._AC_SL1500_.jpg", 36, "seed placeholder");
//                boardGameRepository.save(codenames);
//
//                BoardGame unfair         = new BoardGame("Unfair", 2017, 2, 5, 125,
//                "https://cf.geekdo-images.com/S3ybV1LAp-8SnHIXLLjVqA__thumb/img/oqViRj6nVxK3m36NluTxU1PZkrk=/fit-in/200x150/filters:strip_icc()/pic1534148.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/91k4O5wmMeL._AC_SL1500_.jpg", 35, "seed placeholder");
//                boardGameRepository.save(unfair);
//
//                BoardGame ticketToRide   = new BoardGame("Ticket to Ride", 2004, 2, 5, 60,
//                "https://cf.geekdo-images.com/ZWJg0dCdrWHxVnc0eFXK8w__thumb/img/a9rsFV6KR0aun8GobhRU16aU8Kc=/fit-in/200x150/filters:strip_icc()/pic38668.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/91YNJM4oyhL._AC_SL1500_.jpg", 34, "seed placeholder");
//                boardGameRepository.save(ticketToRide);
//
//                BoardGame pandemic       = new BoardGame("Pandemic", 2008, 2, 4, 45,
//                "https://cf.geekdo-images.com/S3ybV1LAp-8SnHIXLLjVqA__thumb/img/oqViRj6nVxK3m36NluTxU1PZkrk=/fit-in/200x150/filters:strip_icc()/pic1534148.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/814F5EyoMoL._AC_SL1500_.jpg", 33, "seed placeholder");
//                boardGameRepository.save(pandemic);
//
//                BoardGame wingspan       = new BoardGame("Wingspan", 2019, 1, 5, 70,
//                "https://cf.geekdo-images.com/yLZJCVLlIx4c7eJEWUNJ7w__thumb/img/VNToqgS2-pOGU6MuvIkMPKn_y-s=/fit-in/200x150/filters:strip_icc()/pic4458123.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/71S2vTnxqdL._AC_SL1400_.jpg", 32, "seed placeholder");
//                boardGameRepository.save(wingspan);
//
//                BoardGame carcassonne    = new BoardGame("Carcassonne", 2000, 2, 5, 45,
//                "https://cf.geekdo-images.com/Z3upN53-fsVPUDimN9SpOA__thumb/img/_C5pWATlaq3uS8u7IlFb0WMi_ak=/fit-in/200x150/filters:strip_icc()/pic2337577.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/91A3G19ooaL._AC_SL1500_.jpg", 31, "seed placeholder");
//
//                BoardGame dominion       = new BoardGame("Dominion", 2008, 2, 4, 30,
//                "https://cf.geekdo-images.com/j6iQpZ4XkemZP07HNCODBA__thumb/img/B2u2ghwlmI_qsUtCwuvcbnBcIqU=/fit-in/200x150/filters:strip_icc()/pic394356.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/91A3G19ooaL._AC_SL1500_.jpg", 30, "seed placeholder");
//
//                BoardGame dixit          = new BoardGame("Dixit", 2008, 3 , 6, 30,
//                "https://cf.geekdo-images.com/uSgzS-SClISqDkYRCdUq6g__thumb/img/iLgQV17c4xezI4ZeN_hmue_jh40=/fit-in/200x150/filters:strip_icc()/pic3483909.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/71IAp13HzyL._AC_SL1024_.jpg", 29, "seed placeholder");
//
//                BoardGame arkhamHorror   = new BoardGame("Arkham Horror", 2018, 1, 6, 108,
//                "https://cf.geekdo-images.com/ex8DrGtCifqEpkSd2d9ReQ__thumb/img/a9cLg3IglGjWr0xGrRA-yHUh7Zo=/fit-in/200x150/filters:strip_icc()/pic4582151.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/91Fnw-NNDcL._AC_SL1500_.jpg", 28, "seed placeholder");
//
//                BoardGame sevenWonders   = new BoardGame("7 Wonders", 2010, 2 , 7, 30,
//                "https://cf.geekdo-images.com/RvFVTEpnbb4NM7k0IF8V7A__thumb/img/ZlG_SRFChObHenw79fAve56_mnk=/fit-in/200x150/filters:strip_icc()/pic860217.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/81v6X774I3L._AC_SL1500_.jpg", 27, "seed placeholder");
//
//                BoardGame trivialPursuit = new BoardGame("Trivial Pursuit", 1979, 2, 24, 45,
//                "https://cf.geekdo-images.com/D2PAA3KSCkAVo8yRRG_VVA__thumb/img/ibn9l-7FiNCo3KLfPylGQqhzcfc=/fit-in/200x150/filters:strip_icc()/pic872666.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/9105%2Bj6NJhL._AC_SL1500_.jpg", 26, "seed placeholder");
//
//                BoardGame terraformingMars  = new BoardGame("Terraforming Mars", 2016, 1, 5, 120,
//                "https://cf.geekdo-images.com/wg9oOLcsKvDesSUdZQ4rxw__thumb/img/BTxqxgYay5tHJfVoJ2NF5g43_gA=/fit-in/200x150/filters:strip_icc()/pic3536616.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/91RdR7olLsL._AC_SL1500_.jpg", 25, "seed placeholder");
//
//                boardGameRepository.save(carcassonne);
//                boardGameRepository.save(dominion);
//                boardGameRepository.save(dixit);
//                boardGameRepository.save(arkhamHorror);
//                boardGameRepository.save(sevenWonders);
//                boardGameRepository.save(trivialPursuit);
//                boardGameRepository.save(terraformingMars);
//
//                BoardGame root = new BoardGame("Root", 2018, 2, 4, 90,
//                "https://cf.geekdo-images.com/JUAUWaVUzeBgzirhZNmHHw__thumb/img/ACovMZzGGIsBRyEQXFnsT8282NM=/fit-in/200x150/filters:strip_icc()/pic4254509.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/91dLPxrPtTL._AC_SL1500_.jpg", 24, "seed placeholder");
//                boardGameRepository.save(root);
//
//                BoardGame scythe = new BoardGame("Scythe", 2016, 1, 5, 115,
//                "https://cf.geekdo-images.com/7k_nOxpO9OGIjhLq2BUZdA__thumb/img/eQ69OEDdjYjfKg6q5Navee87skU=/fit-in/200x150/filters:strip_icc()/pic3163924.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/918TkODhXPL._AC_SL1500_.jpg", 23, "seed placeholder");
//                boardGameRepository.save(scythe);
//
//                BoardGame risk = new BoardGame("Risk", 1959, 2, 6, 120,
//                "https://cf.geekdo-images.com/Oem1TTtSgxOghRFCoyWRPw__thumb/img/5cltSV60oVvjL3Ag_KTJbmTdU6w=/fit-in/200x150/filters:strip_icc()/pic4916782.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/91dY9-OV2nL._AC_SL1500_.jpg", 22, "seed placeholder");
//                boardGameRepository.save(risk);
//
//                BoardGame mysterium = new BoardGame("Mysterium", 2015, 2, 7, 42,
//                "https://cf.geekdo-images.com/wfeAiLK5n5hD1omhnlYLLA__thumb/img/NAl-NrSAmWvklSVr3hKrSwiszNA=/fit-in/200x150/filters:strip_icc()/pic2601683.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/61yQyxWwIRL._AC_SL1000_.jpg", 21, "seed placeholder");
//                boardGameRepository.save(mysterium);
//
//                BoardGame kingOfTokyo = new BoardGame("King of Tokyo", 2011, 2, 6, 30,
//                "https://cf.geekdo-images.com/m_RzXpHURC0_xLkvRSR_sw__thumb/img/OfgWmkGJj5BgOO0zFHEtP5CHfbU=/fit-in/200x150/filters:strip_icc()/pic3043734.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/81eN66OIlBL._AC_SL1500_.jpg", 20, "seed placeholder");
//                boardGameRepository.save(kingOfTokyo);
//
//                BoardGame loveLetter = new BoardGame("Love Letter", 2012, 2, 4, 20,
//                "https://cf.geekdo-images.com/T1ltXwapFUtghS9A7_tf4g__thumb/img/GtNX7gCmGpw39Tr6JApWC3Aga5U=/fit-in/200x150/filters:strip_icc()/pic1401448.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/714R7c6eCRL._AC_SL1500_.jpg", 19, "seed placeholder");
//                boardGameRepository.save(loveLetter);
//
//                BoardGame talesOfTheArabianNights = new BoardGame("Tales of the Arabian Nights", 2009, 1, 6, 120,
//                "https://cf.geekdo-images.com/ibYzk1WgVEEnC0hnXdpqpg__thumb/img/IOnGg9i6oaoF2Ua8RtEyb5H593A=/fit-in/200x150/filters:strip_icc()/pic486114.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/81y9avkYekL._AC_SL1500_.jpg", 18, "seed placeholder");
//                boardGameRepository.save(talesOfTheArabianNights);
//
//                BoardGame explodingKittens = new BoardGame("Exploding Kittens", 2015, 2, 5, 15,
//                "https://cf.geekdo-images.com/N8bL53-pRU7zaXDTrEaYrw__thumb/img/3tH4pIc1Udzkd0tXc6MgVQ59BC0=/fit-in/200x150/filters:strip_icc()/pic2691976.png",
//                "https://images-na.ssl-images-amazon.com/images/I/71UUDAPpKWL._AC_SL1500_.jpg", 17, "seed placeholder");
//                boardGameRepository.save(explodingKittens);
//
//                BoardGame deadOfWinter = new BoardGame("Dead of Winter", 2014, 2, 5, 120,
//                "https://cf.geekdo-images.com/g4mV4BH-ZrhMUVgil-yV1A__thumb/img/foFGN8KwmN8BaYNpolq0HcMba4M=/fit-in/200x150/filters:strip_icc()/pic3016500.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/718SP3giK8L._AC_SL1024_.jpg", 16, "seed placeholder");
//                boardGameRepository.save(deadOfWinter);
//
//                BoardGame secretHitler = new BoardGame("Secret Hitler", 2016, 5, 10, 45,
//                "https://cf.geekdo-images.com/rAQ3hIXoH6xDcj41v9iqCg__thumb/img/xA2T7PiwN3Z8pwAksicoCOA1tf0=/fit-in/200x150/filters:strip_icc()/pic5164305.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/615kBmrdGRL._AC_SL1000_.jpg", 15, "seed placeholder");
//                boardGameRepository.save(secretHitler);
//
//                BoardGame werewolf = new BoardGame("One Night Ultimate Werewolf", 2014, 3, 10, 10,
//                "https://cf.geekdo-images.com/KLDb0vR3w8mfaHgIGF0gHw__thumb/img/ZgQsHsvdxXZr4DbMGUBSTArBp3o=/fit-in/200x150/filters:strip_icc()/pic1809823.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/613ClIZLD4L._AC_SL1024_.jpg", 14, "seed placeholder");
//                boardGameRepository.save(werewolf);
//
//                BoardGame wavelength = new BoardGame("Wavelength", 2019, 2, 12, 45,
//                "https://cf.geekdo-images.com/z4fbPdmJg_5yphJEvql4ZA__thumb/img/bxLuHVLF-Y06jWRSKA-tuQ3YYcE=/fit-in/200x150/filters:strip_icc()/pic4552862.png",
//                "https://images-na.ssl-images-amazon.com/images/I/91QcesYc0CL._AC_SL1500_.jpg", 13, "seed placeholder");
//                boardGameRepository.save(wavelength);
//
//                BoardGame monopoly = new BoardGame("Monopoly", 1933, 2, 8, 180,
//                "https://cf.geekdo-images.com/9nGoBZ0MRbi6rdH47sj2Qg__thumb/img/ezXcyEsHhS9iRxmuGe8SmiLLXlM=/fit-in/200x150/filters:strip_icc()/pic5786795.jpg",
//                "https://images-na.ssl-images-amazon.com/images/I/81qy%2BMXuxDL._AC_SL1392_.jpg", 12, "seed placeholder");
//                boardGameRepository.save(monopoly);
