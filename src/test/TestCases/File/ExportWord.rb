#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("Verifie l'export d'un fichier word")
severity("normal")

def test

    with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/Biblio.xml\"]")
        click("tableBook", "{\"cell\":[2,\"Parution\"]}")
        click("tableBook", "{\"cell\":[2,\"Titre\"]}")
        click("ModifButton")
    }

    with_window("Modification d'un Livre") {
        select("pret", "true")
        click("btvalider")
    }

    with_window("Bibliotheque") {
        select_fx_menu("#menu", "File>>Export Word")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/export.DOCX\"]")
    }

end
