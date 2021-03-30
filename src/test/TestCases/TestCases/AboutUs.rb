#{{{ Marathon
require_fixture 'default'
#}}} Marathon

severity("normal")

def test

    with_window("Bibliothèque") {
        select_fx_menu("#menu", "About>>About us")
    }

    with_window("Trombinoscope") {
        click("d", 1, 50, 22)
        click("d_2", 1, 53, 71)
        click("d_3", 1, 40, 66)
        window_closed("Trombinoscope")
    }

end
