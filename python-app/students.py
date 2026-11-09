import os
from sqlalchemy import create_engine, text


def get_engine():
    # Exemple : postgresql+psycopg2://postgres:postgres@localhost:5432/tp
    return create_engine(os.environ["DATABASE_URL"], pool_pre_ping=True)


def add_student(engine, name, grade):
    with engine.begin() as conn:
        res = conn.execute(
            text("INSERT INTO students (name, grade) VALUES (:name, :grade) RETURNING id"),
            {"name": name, "grade": grade},
        )
        return res.scalar()


def get_student(engine, student_id):
    with engine.connect() as conn:
        row = conn.execute(
            text("SELECT name, grade FROM students WHERE id = :id"),
            {"id": student_id},
        ).fetchone()
    if row is None:
        return None
    return {"name": row[0], "grade": float(row[1])}


def average_grade(engine):
    with engine.connect() as conn:
        result = conn.execute(text("SELECT AVG(grade) FROM students")).scalar()
    return float(result) if result is not None else None
