#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("Verifie si l'application se ferme quand on appui sur Oui")
severity("normal")

def test

    with_window("Bibliotheque") {
        select_fx_menu("#menu", "File>>Quit")
    }

end
