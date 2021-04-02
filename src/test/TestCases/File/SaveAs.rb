#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("Modifie un fichier existant et crée une nouvelle sauvegarde de ce fichier, ensuite on réouvre ce fichier pour vérifier si il a bien été sauvegardé")
severity("normal")

def test
randnum=rand(1000)
    with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/IdeaProjects/biblio/Biblio.xml\"]")
        click("tableBook", "{\"cell\":[2,\"Titre\"]}")
        click("ModifButton")
    }

    with_window("Modification d'un Livre") {
        select("RangeeInput", "3")
        click("btvalider")
    }

    with_window("Bibliotheque") {
        select_fx_menu("#menu", "File>>Save as")
        select_file_chooser("#filechooser", "[\"#H/IdeaProjects/biblio/biblio"+randnum.to_s+".XML\"]")
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/IdeaProjects/biblio/Biblio.xml\"]")
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/IdeaProjects/biblio/biblio"+randnum.to_s+".XML\"]")
    }

end
