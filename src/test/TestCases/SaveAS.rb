#{{{ Marathon
require_fixture 'default'
#}}} Marathon


description("permet de vérifier si cela crée un nouveau fichier sauvegarder")
severity("normal")

def test
    
    with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/Biblio.xml\"]")
        select_fx_menu("#menu", "File>>Save as")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/testSave.XML\"]")
        click("tableBook", "{\"cell\":[2,\"Auteur\"]}")
        click("SuppButton")
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/testSave.XML\"]")
        
    }

end
