<?php

use PHPUnit\Framework\TestCase;

require_once __DIR__ . '/../Students.php';

class StudentsTest extends TestCase
{
    private PDO $pdo;

    protected function setUp(): void
    {
        $this->pdo = Students::connect();
        $this->pdo->exec('DELETE FROM students');
    }

    public function testAjoutEtLecture(): void
    {
        $id = Students::addStudent($this->pdo, 'Sara', 17);
        $student = Students::getStudent($this->pdo, $id);
        $this->assertSame('Sara', $student['name']);
        $this->assertSame(17.0, $student['grade']);
    }

    public function testEtudiantInexistant(): void
    {
        $this->assertNull(Students::getStudent($this->pdo, 99999));
    }

    public function testMoyenne(): void
    {
        Students::addStudent($this->pdo, 'Amel', 10);
        Students::addStudent($this->pdo, 'Karim', 14);
        $this->assertSame(12.0, Students::averageGrade($this->pdo));
    }
}
