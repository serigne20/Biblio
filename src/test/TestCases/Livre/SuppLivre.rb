#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("Verifie la suppression d'un livre")
severity("normal")

def test
    with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/IdeaProjects/biblio/Biblio.xml\"]")
        click("tableBook", "{\"cell\":[2,\"Titre\"]}")
        click("SuppButton")
    }


end
