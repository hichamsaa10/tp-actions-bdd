import pytest
from sqlalchemy import text
from students import get_engine, add_student, get_student, average_grade


@pytest.fixture
def conn():
    # Connexion à la base, fournie à chaque test.
    # La table est vidée avant chaque test : chaque test part d'une table vide.
    eng = get_engine()
    with eng.begin() as c:
        c.execute(text("DELETE FROM students"))
    yield eng
    eng.dispose()


def test_ajout_et_lecture(conn):
    student_id = add_student(conn, "Sara", 17)
    student = get_student(conn, student_id)
    assert student["name"] == "Sara"
    assert student["grade"] == 17.0


def test_etudiant_inexistant(conn):
    assert get_student(conn, 99999) is None


def test_moyenne(conn):
    add_student(conn, "Amel", 10)
    add_student(conn, "Karim", 14)
    assert average_grade(conn) == 12.0
