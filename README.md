# darwin-world

# Plan działania

## Obiekty na Mapie

### Roślina
- Koordynaty x i y

### Zwierzę
- koordynaty x i y
- energia
- direction (0-7)
- geny (0-7)

**Dodatkowe z wymagań poniżej podczas obserwowania animala**:
- typ genomu,
- która część genomu jest aktywowana,
- ile zjadł roślin, 
- ile posiada dzieci, 
- ile posiada potomków (niekoniecznie będących bezpośrednio dziećmi), 
- ile dni już żyje (jeżeli żyje), 
- którego dnia zmarło (jeżeli żywot już skończyło).


**Energia** - ile dni funkcjonowania obecnie pozostało.Uzupełniana zjadaniem roślin. Zakładamy, że zwierzak zjada roślinę, gdy stanie na jej polu, a jego energia wzrasta wtedy o z góry zdefiniowaną wartość.

**Direction** - Obrót `0` oznacza, że zwierzak nie zmienia swojej orientacji, obrót `1` oznacza, że zwierzak obraca się o 45°, `2`, o 90°, itd. Przykładowo: jeśli zwierzak był skierowany na północ i obrót wynosi `1`, to zwierzak skierowany jest teraz na północny wschód.

**Geny** - Każdy zwierzak ma N genów, z których każdy jest jedną liczbą z zakresu od `0` do `7`. Geny opisują schemat zachowania danej istoty. Podczas każdego ruchu zwierzak zmienia najpierw swoje ustawienie, obracając się zgodnie z aktualnie aktywnym genem, a potem porusza się o jedno pole w wyznaczonym kierunku. Następnie gen ulega dezaktywacji, a aktywuje się gen na prawo od niego (będzie sterował zwierzakiem kolejnego dnia). Gdy geny skończą się, to aktywacja wraca na początek ich listy. Przykładowo - genom:
`0 0 7 0 4`
oznacza, że żyjątko będzie kolejno: szło przed siebie, szło przed siebie, szło nieco w lewo, szło przed siebie, zawracało, szło przed siebie, ... - itd.

**Rozmnażanie** - zwierzaki będą się rozmnażać tylko jeśli mają odpowiednią ilość energii. Przy reprodukcji rodzice tracą na rzecz młodego pewną część swojej energii - ta energia będzie rónocześnie stanowić startową energię ich potomka. Urodzone zwierzę otrzymuje genotyp będący krzyżówką genotypów rodziców. Udział genów jest proporcjonalny do energii rodziców i wyznacza miejsce podziału genotypu. Przykładowo, jeśli jeden rodzic ma 50, a  drugi 150 punktów energii, to dziecko otrzyma 25% genów pierwszego oraz 75% genów drugiego rodzica. Udział ten określa miejsce przecięcia genotypu, przyjmując, że geny są uporządkowane. W pierwszym kroku losowana jest strona genotypu, z której zostanie wzięta część osobnika silniejszego, np. *prawa*. W tym przypadku dziecko otrzymałoby odcinek obejmujący 25% *lewych* genów pierwszego rodzica oraz 75% *prawych* genów drugiego rodzica. Jeśli jednak wylosowana byłaby strona *lewa*, to dziecko otrzymałoby 75% *lewych* genów silniejszego osobnika oraz 25% *prawych* genów. Na koniec mają zaś miejsce mutacje: losowa liczba (wybranych również losowo) genów potomka zmienia swoje wartości na zupełnie nowe.

## Symulacja
Symulacja każdego dnia składa się z poniższej sekwencji kroków:

1. Usunięcie martwych zwierzaków z mapy.
2. Skręt i przemieszczenie każdego zwierzaka.
3. Konsumpcja roślin, na których pola weszły zwierzaki.
4. Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
5. Wzrastanie nowych roślin na wybranych polach mapy.

Daną symulację opisuje szereg parametrów:

* wysokość i szerokość mapy,
* wariant mapy (wyjaśnione w sekcji poniżej),
* startowa liczba roślin,
* energia zapewniana przez zjedzenie jednej rośliny,
* liczba roślin wyrastająca każdego dnia,
* wariant wzrostu roślin (wyjaśnione w sekcji poniżej),
* startowa liczba zwierzaków,
* startowa energia zwierzaków,
* energia konieczna, by uznać zwierzaka za najedzonego (i gotowego do rozmnażania),
* energia rodziców zużywana by stworzyć potomka,
* minimalna i maksymalna liczba mutacji u potomków (może być równa `0`),
* wariant mutacji (wyjaśnione w sekcji poniżej),
* długość genomu zwierzaków,
* wariant zachowania zwierzaków (wyjaśnione w sekcji poniżej).

### Mapa
* **kula ziemska** - lewa i prawa krawędź mapy zapętlają się (jeżeli zwierzak wyjdzie za lewą krawędź, to pojawi się po prawej stronie - a jeżeli za prawą, to po lewej); górna i dolna krawędź mapy to bieguny - nie można tam wejść (jeżeli zwierzak próbuje wyjść poza te krawędzie mapy, to pozostaje na polu na którym był, a jego kierunek zmienia się na odwrotny);
* **pożary** - co jakąś (zadaną w konfiguracji) liczbę tur na mapie pojawia się pożar. Pożar zaczyna się na jednym polu z rośliną i w każdej turze rozprzestrzenia się na wszystkie przylegające do niej rośliny (ale nie po skosie). Pożar na każdym polu trwa stałą zadaną (konfigurowalną) liczbę tur i po jego zakończeniu roślina na tym polu znika. Jeśli zwierzak wejdzie na pole z ogniem, umiera.

### Roślinność
Podział pól na preferowane i niepreferowane. Istnieje 80% szansy, że nowa roślina wyrośnie na preferowanym polu, a tylko 20% szans, że wyrośnie na polu drugiej kategorii. Preferowanych jest około 20% wszystkich miejsc na mapie, 80% miejsc jest uznawane za nieatrakcyjne. 
* **zalesione równiki** - preferowany przez rośliny jest poziomy pas pól w centralnej części mapy (udający równik i okolice);

### Zwierzaki
* **pełna losowość** - mutacja zmienia gen na dowolny inny gen

* **pełna predestynacja** - zwierzak zawsze wykonuje kolejno geny, jeden po drugim;

* **nieco szaleństwa** - w 80% przypadków zwierzak po wykonaniu genu aktywuje gen następujący zaraz po nim, w 20% przypadków przeskakuje jednak do innego, losowego genu;

**Przykład realizaji**
Jeśli zespół projektowy otrzymał do realizacji projekt w wariancie B-3 to znaczy, że:
- musi zapewnić w konfiguracji symulacji możliwość wyboru między mapą _kula ziemska_ a _pożary_,
- musi zapewnić w konfiguracji symulacji możliwość wyboru zachowania zwierzaka: _pełna predestynacja_ lub _nieco szaleństwa_,
- w symulacji rośliny zawsze rosną zgodnie ze strategią _zalesione równiki_, a mutacje zwierząt są _w pełni losowe_ (brak dodatkowej konfiguracji)


1. Aplikacja ma być realizowana z użyciem graficznego interfejsu użytkownika z wykorzystaniem biblioteki JavaFX.
2. Jej głównym zadaniem jest umożliwienie uruchamiania symulacji o wybranych konfiguracjach.
    1. Powinna umożliwić wybranie jednej z uprzednio przygotowanych gotowych konfiguracji,
   
    1. "wyklikanie" nowej konfiguracji,
    1. oraz zapisanie jej do ponownego użytku w przyszłości. \
   `Predefiniowana konfiguracja + zapis i odczyt z plików JSON`
3. Uruchomienie symulacji powinno skutkować pojawieniem się nowego okna obsługującego daną symulację.
    1. Jednocześnie uruchomionych może być wiele symulacji, każda w swoim oknie, każda na osobnej mapie.  
   `Trzeba zrealizować to co w dodatkowym zadaniu w ostatnim labie, albo coś bardzo podobnego. Ogarnąć jak obsługiwć wiele okien w JavaFX i mieć komunikację między nimi.`
4. Sekcja symulacji ma wyświetlać animację pokazującą pozycje zwierzaków, ich energię w dowolnej formie (np. koloru lub paska zdrowia) oraz pozycje roślin - i ich zmiany.   
`Observer, wywoływany za każdym razem, gdy zmienia się coś na mapie i aktualizujący wyizualizacje`
5. Program musi umożliwiać zatrzymywanie oraz wznawianie animacji w dowolnym momencie (niezależnie dla każdej mapy - patrz niżej).  
`Jakieś zatrzymanie wątku, interrupt??? Do ogarnięcia głębszego`
6. Program ma pozwalać na śledzenie następujących statystyk dla aktualnej sytuacji w symulacji:
    * liczby wszystkich zwierzaków,
    * liczby wszystkich roślin,
    * liczby wolnych pól,
    * najpopularniejszych genotypów,
    * średniego poziomu energii dla żyjących zwierzaków,
    * średniej długości życia zwierzaków dla martwych zwierzaków (wartość uwzględnia wszystkie nieżyjące zwierzaki - od początku symulacji),
    * średniej liczby dzieci dla żyjących zwierzaków (wartość uwzględnia wszystkie powstałe zwierzaki, a nie tylko zwierzaki powstałe w danej epoce).  
   `Liczba zwierzaków - długość tablicy zwierzaków albo observery`  
   `Liczba roślin - długość tablicy roślin albo observery`  
   `Liczba wolnych pól`  
   `Najpopularniejszy genotyp - hashmapa z genotypami jako kluczami i zliczanie + jakieś sortowanie tego by sie przydało`  
   `Średnia długość życia nieżyjących zwierzaków - suma długości nieżyjących + counter nieżyjących`  
   `średnia liczba dzieci zwierzaków - podobnie jak u góry`
7. Po zatrzymaniu programu można oznaczyć jednego zwierzaka jako wybranego do śledzenia. Od tego momentu (do zatrzymania śledzenia) UI powinien przekazywać nam informacje o jego statusie i historii:
    * jaki ma genom,
    * która jego część jest aktywowana,
    * ile ma energii,
    * ile zjadł roślin,
    * ile posiada dzieci,
    * ile posiada potomków (niekoniecznie będących bezpośrednio dziećmi),
    * ile dni już żyje (jeżeli żyje),
    * którego dnia zmarło (jeżeli żywot już skończyło).  
   `Trzeba przechowywać każdą z tych informacji dla zwierzak, zrobić gettery i mieć funkcje, do której sie przekaże zwierazka i która będzie zczytywałą dane przy każdym update`
8. Po zatrzymaniu programu powinno być też możliwe:
    * pokazanie, które ze zwierząt mają dominujący (najpopularniejszy) genotyp (np. poprzez wyróżnienie ich wizualnie),
    * pokazanie, które z pól są preferowane przez rośliny (np. poprzez wyróżnienie ich wizualnie).  
   `Hashmapa Genotyp->Animal?`
   `Czy na pałe iterowanie przez wszystko na mapie?`
9. Jeżeli zdecydowano się na to w momencie uruchamiania symulacji, to jej statystyki powinny być zapisywane (każdego dnia) do pliku CSV. Plik ten powinnien być "otwieralny" przez dowolny rozujmiejący ten format program (np. MS Excel).
10. Aplikacja powinna być możliwa do zbudowania i uruchomienia z wykorzystaniem Gradle'a.
