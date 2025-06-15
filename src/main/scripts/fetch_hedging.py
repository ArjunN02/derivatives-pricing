import psycopg2
import argparse
import csv
import json
from datetime import datetime
from rich.console import Console
from rich.table import Table
from rich.style import Style

# Setup
console = Console()

# Parse arguments
parser = argparse.ArgumentParser(description="View & export hedging decisions")
parser.add_argument("--symbol", help="Filter by symbol, e.g., AAPL")
parser.add_argument("--model", help="Filter by pricing model, e.g., BlackScholes")
parser.add_argument("--date", help="Filter by date (YYYY-MM-DD)")
parser.add_argument("--export", choices=["csv", "json"], help="Export results")
args = parser.parse_args()

# Connect to PostgreSQL
conn = psycopg2.connect(
   dbname="pricing_db",
   user="postgres",
   password="arjun@123",
   host="localhost",
   port="5432"
)
cur = conn.cursor()

# Build query
query = "SELECT action, model, price, symbol, timestamp FROM hedging_log WHERE 1=1"
params = []

if args.symbol:
    query += " AND symbol = %s"
    params.append(args.symbol.upper())

if args.model:
    query += " AND model = %s"
    params.append(args.model)

if args.date:
    try:
        date_obj = datetime.strptime(args.date, "%Y-%m-%d").date()
        query += " AND DATE(timestamp) = %s"
        params.append(date_obj)
    except ValueError:
        console.print("[red]Invalid date format. Use YYYY-MM-DD.[/red]")
        exit(1)

query += " ORDER BY timestamp DESC LIMIT 50"
cur.execute(query, params)
rows = cur.fetchall()
cur.close()
conn.close()

# Optional export
if args.export == "csv":
    with open("hedging_export.csv", "w", newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["Action", "Model", "Price", "Symbol", "Timestamp"])
        writer.writerows(rows)
    console.print("[bold green]Exported to hedging_export.csv[/bold green]")
    exit(0)

if args.export == "json":
    data = [
        {
            "action": row[0],
            "model": row[1],
            "price": row[2],
            "symbol": row[3],
            "timestamp": str(row[4])
        }
        for row in rows
    ]
    with open("hedging_export.json", "w") as f:
        json.dump(data, f, indent=2)
    console.print("[bold green]Exported to hedging_export.json[/bold green]")
    exit(0)

# Display using rich
table = Table(title="Hedging Decisions", style="bold cyan")
table.add_column("Symbol", style="white")
table.add_column("Price", style="yellow", justify="right")
table.add_column("Model", style="green")
table.add_column("Action", style="bold")
table.add_column("Timestamp", style="dim")

for action, model, price, symbol, timestamp in rows:
    if action == "BUY STOCK":
        color = Style(color="green", bold=True)
    elif action == "SELL STOCK":
        color = Style(color="red", bold=True)
    else:
        color = Style(color="yellow", bold=True)

    table.add_row(
        symbol,
        f"{price:.2f}",
        model,
        f"[{color}]{action}[/{color}]",
        str(timestamp)
    )

console.print(table)
