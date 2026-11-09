const { Pool } = require("pg");

function getPool() {
  return new Pool({
    host: "localhost",
    port: 5432,
    user: "postgres",
    password: "postgres",
    database: "tp",
  });
}

async function addStudent(pool, name, grade) {
  const res = await pool.query(
    "INSERT INTO students (name, grade) VALUES ($1, $2) RETURNING id",
    [name, grade]
  );
  return res.rows[0].id;
}

async function getStudent(pool, id) {
  const res = await pool.query(
    "SELECT name, grade FROM students WHERE id = $1",
    [id]
  );
  if (res.rows.length === 0) return null;
  return { name: res.rows[0].name, grade: parseFloat(res.rows[0].grade) };
}

async function averageGrade(pool) {
  const res = await pool.query("SELECT AVG(grade) AS avg FROM students");
  const avg = res.rows[0].avg;
  return avg === null ? null : parseFloat(avg);
}

module.exports = { getPool, addStudent, getStudent, averageGrade };
