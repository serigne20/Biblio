#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("Verifie lorsqu'on clique sur NON dans la box pour quitter l'app, qu'on ne quitte pas l'app")
severity("normal")

def test

    with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/Biblio.xml\"]")
        select_fx_menu("#menu", "File>>Quit")
    }

    with_window("Quitter") {
        click("closeButton")
    }

end
