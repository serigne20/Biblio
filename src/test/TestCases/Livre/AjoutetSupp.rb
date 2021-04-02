#{{{ Marathon
require_fixture 'default'
#}}} Marathon

def test

    with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/Biblio.xml\"]")
        click("AjoutButton")
    }

    with_window("Ajout d'un Livre") {
        select("TitreInput", "ajoouttest")
        select("AuteurInput", "auteurtest")
        select("ParutionInput", "1900")
        select("ColonneInput", "4")
        select("RangeeInput", "4")
        select("EditeurInput", "EditeurTest")
        select("FormatInput", "FormatTest")
        select("ResumeInput", "RésuméTest")
        select("pret", "true")
        click("btvalider")
    }

    with_window("Bibliotheque") {
        click("tableBook", "{\"cell\":[3,\"Titre\"]}")
        click("SuppButton")
    }

end
