const { getPool, addStudent, getStudent, averageGrade } = require("./students");

let pool;

beforeAll(() => {
  pool = getPool();
});

afterAll(async () => {
  await pool.end();
});

beforeEach(async () => {
  await pool.query("DELETE FROM students");
});

test("ajoute un étudiant puis le relit", async () => {
  const id = await addStudent(pool, "Sara", 17);
  const student = await getStudent(pool, id);
  expect(student.name).toBe("Sara");
  expect(student.grade).toBe(17);
});

test("étudiant inexistant : null", async () => {
  expect(await getStudent(pool, 99999)).toBeNull();
});

test("moyenne de 10 et 14 : 12", async () => {
  await addStudent(pool, "Amel", 10);
  await addStudent(pool, "Karim", 14);
  expect(await averageGrade(pool)).toBe(12);
});
