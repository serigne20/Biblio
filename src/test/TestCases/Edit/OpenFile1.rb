#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("permet d'ouvrir un premier fichier")
severity("normal")

def test
    with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/Biblio.xml\"]")
    }

end
