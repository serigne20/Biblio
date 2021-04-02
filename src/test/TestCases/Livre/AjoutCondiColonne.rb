#{{{ Marathon
require_fixture 'default'
#}}} Marathon

def test
  randnum=rand(1000)
      with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/IdeaProjects/biblio/Biblio.xml\"]")
        click("AjoutButton")
    }

    with_window("Ajout d'un Livre") {
        select("TitreInput", "TESTColo")
        select("AuteurInput", "AutColo")
        select("ParutionInput", "1000")
        select("ColonneInput", randnum.to_s)
        select("RangeeInput", "4")
        select("EditeurInput", "EditeurCo")
        select("FormatInput", "FormatCO")
        select("ResumeInput", "RésuméCo")
        select("pret", "true")
        click("btvalider")
    }

    with_window("Erreur") {
        click("CloseButton")
    }



end
