#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("Permet de vérifier si la sauvegarde fonctionne bien ")
severity("normal")

def test

    with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/testSave.XML\"]")
        click("tableBook", "{\"cell\":[0,\"Auteur\"]}")
        click("SuppButton")
        select_fx_menu("#menu", "File>>Save")
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/Biblio.xml\"]")
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/testSave.XML\"]")
    }

end
