#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("Vérifie la modification d'une donnée d'un livre")
severity("normal")

def test
    with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/IdeaProjects/biblio/Biblio.xml\"]")
        click("tableBook", "{\"cell\":[2,\"Titre\"]}")
        click("ModifButton")
    }

    with_window("Modification d'un Livre") {
        select("RangeeInput", "2")
        click("btvalider")
    }

end
