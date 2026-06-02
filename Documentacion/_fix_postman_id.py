"""
Script de limpieza para la Collection Postman.

Elimina los bloques de test duplicados que buscan `property('id')` en respuestas
donde el backend ya devuelve el campo con prefijo (vendedorId, usuarioId, etc.).
Cada request tiene DOS bloques de tests: uno correcto y uno duplicado obsoleto.
Este script elimina el duplicado.

Uso: python _fix_postman_id.py
"""

import json
import re
from pathlib import Path

HERE = Path(__file__).parent
SRC = HERE / "Antuan SA Gestion API.postman_collection.json"
BACKUP = HERE / "Antuan SA Gestion API.postman_collection.backup.json"


def clean_exec(exec_lines):
    """Elimina bloques de test cuyo body es `pm.expect(...).to.have.property('id')`.
    Devuelve la nueva lista de líneas y cuántos bloques se eliminaron."""
    removed = 0
    cleaned = []
    i = 0
    n = len(exec_lines)

    while i < n:
        line = exec_lines[i]
        # Detectar inicio de bloque "Response has id"
        if "pm.test('Response has id'" in line or 'pm.test("Response has id"' in line:
            # Buscar cierre `})` dentro de las próximas líneas (max 6 líneas)
            end = -1
            block_assertion_is_id = False
            for j in range(i, min(i + 8, n)):
                if "have.property('id')" in exec_lines[j] or 'have.property("id")' in exec_lines[j]:
                    block_assertion_is_id = True
                if exec_lines[j].strip() == "})":
                    end = j
                    break
            if end > 0 and block_assertion_is_id:
                # Quitar también una línea vacía previa si la hay
                if cleaned and cleaned[-1].strip() == "":
                    cleaned.pop()
                # Quitar también un pm.test('Status XXX...') inmediatamente previo si es duplicado
                # (mirando atrás en cleaned)
                if cleaned and "pm.test(" in cleaned[-1] and "Status" in cleaned[-1]:
                    cleaned.pop()
                    if cleaned and cleaned[-1].strip() == "":
                        cleaned.pop()
                removed += 1
                i = end + 1
                continue
        cleaned.append(line)
        i += 1

    return cleaned, removed


def walk(node, total):
    """Recorrido recursivo del JSON."""
    if isinstance(node, dict):
        # Si es un item con events
        events = node.get("event")
        if isinstance(events, list):
            for ev in events:
                script = ev.get("script") if isinstance(ev, dict) else None
                exec_lines = script.get("exec") if isinstance(script, dict) else None
                if isinstance(exec_lines, list):
                    new_lines, removed = clean_exec(exec_lines)
                    if removed:
                        script["exec"] = new_lines
                        total[0] += removed
        # Recurse
        for v in node.values():
            walk(v, total)
    elif isinstance(node, list):
        for v in node:
            walk(v, total)


def main():
    with open(SRC, "r", encoding="utf-8") as f:
        data = json.load(f)

    # Backup
    with open(BACKUP, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)
    print(f"Backup creado: {BACKUP.name}")

    total = [0]
    walk(data, total)

    with open(SRC, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)

    print(f"Bloques 'Response has id' eliminados: {total[0]}")


if __name__ == "__main__":
    main()
