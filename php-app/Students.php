<?php

class Students
{
    public static function connect(): PDO
    {
        $pdo = new PDO(
            'pgsql:host=localhost;port=5432;dbname=tp',
            'postgres',
            'postgres'
        );
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        return $pdo;
    }

    public static function addStudent(PDO $pdo, string $name, float $grade): int
    {
        $st = $pdo->prepare(
            'INSERT INTO students (name, grade) VALUES (?, ?) RETURNING id'
        );
        $st->execute([$name, $grade]);
        return (int) $st->fetchColumn();
    }

    public static function getStudent(PDO $pdo, int $id): ?array
    {
        $st = $pdo->prepare('SELECT name, grade FROM students WHERE id = ?');
        $st->execute([$id]);
        $row = $st->fetch(PDO::FETCH_ASSOC);
        if ($row === false) {
            return null;
        }
        return ['name' => $row['name'], 'grade' => (float) $row['grade']];
    }

    public static function averageGrade(PDO $pdo): ?float
    {
        $avg = $pdo->query('SELECT AVG(grade) FROM students')->fetchColumn();
        return $avg === null ? null : (float) $avg;
    }
}
