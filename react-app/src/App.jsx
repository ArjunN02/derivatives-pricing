import React, { useState } from "react";
import {
  Container,
  Box,
  Typography,
  TextField,
  MenuItem,
  Button,
  CircularProgress,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Select,
  InputLabel,
  FormControl,
} from "@mui/material";

const models = ["BlackScholes", "BinomialTree"];
const exportFormats = ["csv", "json"];

export default function App() {
  const [symbol, setSymbol] = useState("");
  const [spotPrice, setSpotPrice] = useState("");
  const [strikePrice, setStrikePrice] = useState("");
  const [volatility, setVolatility] = useState("");
  const [riskFreeRate, setRiskFreeRate] = useState("");
  const [timeToMaturity, setTimeToMaturity] = useState("");
  const [isCall, setIsCall] = useState(true);
  const [model, setModel] = useState(models[0]);
  const [exportFormat, setExportFormat] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [rows, setRows] = useState([]);
  const [hedgingRows, setHedgingRows] = useState([]);

  const API_URL = "http://localhost:8080/api/quotes";
  const HEDGING_API_URL = "http://localhost:8080/api/quotes/hedging-actions";
  const HEDGING_LATEST_API_URL =
    "http://localhost:8080/api/quotes/hedging-actions/latest";
  const fetchData = async () => {
    setLoading(true);
    setError("");
    try {
      // Pricing result
      const reqBody = {
        symbol,
        spotPrice: parseFloat(spotPrice),
        strikePrice: parseFloat(strikePrice),
        volatility: parseFloat(volatility),
        riskFreeRate: parseFloat(riskFreeRate),
        timeToMaturity: parseFloat(timeToMaturity),
        isCall,
        model,
      };
      const res = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(reqBody),
      });
      if (!res.ok) throw new Error("API error");
      const data = await res.json();
      setRows([data]); // Only one result per request

      // Hedging action for this pricing result
      const params = new URLSearchParams();
      params.append("symbol", symbol);
      params.append("model", model);
      const hedgingRes = await fetch(
        `${HEDGING_LATEST_API_URL}?${params.toString()}`
      );
      if (!hedgingRes.ok) throw new Error("Hedging API error");
      const hedgingData = await hedgingRes.json();
      setHedgingRows(hedgingData ? [hedgingData] : []);
    } catch (e) {
      setError("Failed to fetch data.");
    }
    setLoading(false);
  };

  const handleExport = () => {
    if (exportFormat === "csv") {
      const pricingCsv = [
        ["Symbol", "Price", "Model", "Timestamp", "Delta", "Gamma"],
        ...rows.map((r) => [
          r.symbol,
          r.price,
          r.model,
          r.timestamp,
          r.delta,
          r.gamma,
        ]),
      ]
        .map((e) => e.join(","))
        .join("\n");

      const hedgingCsv = [
        ["Symbol", "Price", "Model", "Action", "Timestamp"],
        ...hedgingRows.map((r) => [
          r.symbol,
          r.price,
          r.model,
          r.action,
          r.timestamp,
        ]),
      ]
        .map((e) => e.join(","))
        .join("\n");

      const blob = new Blob([pricingCsv + "\n\n" + hedgingCsv], {
        type: "text/csv",
      });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "pricing_and_hedging_export.csv";
      a.click();
      window.URL.revokeObjectURL(url);
    } else if (exportFormat === "json") {
      const blob = new Blob(
        [JSON.stringify({ pricing: rows, hedging: hedgingRows }, null, 2)],
        { type: "application/json" }
      );
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "pricing_and_hedging_export.json";
      a.click();
      window.URL.revokeObjectURL(url);
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 6 }}>
      <Paper elevation={4} sx={{ p: 4, borderRadius: 3 }}>
        <Typography variant="h5" gutterBottom fontWeight={700}>
          Option Pricing Service
        </Typography>
        <Box
          component="form"
          sx={{ display: "flex", gap: 2, flexWrap: "wrap", mb: 3 }}
        >
          <TextField
            label="Symbol"
            value={symbol}
            onChange={(e) => setSymbol(e.target.value)}
            placeholder="AAPL"
            sx={{ minWidth: 120 }}
          />
          <TextField
            label="Spot Price"
            value={spotPrice}
            onChange={(e) => setSpotPrice(e.target.value)}
            type="number"
            sx={{ minWidth: 120 }}
          />
          <TextField
            label="Strike Price"
            value={strikePrice}
            onChange={(e) => setStrikePrice(e.target.value)}
            type="number"
            sx={{ minWidth: 120 }}
          />
          <TextField
            label="Volatility"
            value={volatility}
            onChange={(e) => setVolatility(e.target.value)}
            type="number"
            sx={{ minWidth: 120 }}
          />
          <TextField
            label="Risk-Free Rate"
            value={riskFreeRate}
            onChange={(e) => setRiskFreeRate(e.target.value)}
            type="number"
            sx={{ minWidth: 120 }}
          />
          <TextField
            label="Time to Maturity (years)"
            value={timeToMaturity}
            onChange={(e) => setTimeToMaturity(e.target.value)}
            type="number"
            sx={{ minWidth: 120 }}
          />
          <FormControl sx={{ minWidth: 120 }}>
            <InputLabel>Type</InputLabel>
            <Select
              value={isCall ? "Call" : "Put"}
              label="Type"
              onChange={(e) => setIsCall(e.target.value === "Call")}
            >
              <MenuItem value="Call">Call</MenuItem>
              <MenuItem value="Put">Put</MenuItem>
            </Select>
          </FormControl>
          <FormControl sx={{ minWidth: 150 }}>
            <InputLabel>Model</InputLabel>
            <Select
              value={model}
              label="Model"
              onChange={(e) => setModel(e.target.value)}
            >
              {models.map((m) => (
                <MenuItem key={m} value={m}>
                  {m}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl sx={{ minWidth: 120 }}>
            <InputLabel>Export</InputLabel>
            <Select
              value={exportFormat}
              label="Export"
              onChange={(e) => setExportFormat(e.target.value)}
            >
              {exportFormats.map((f) => (
                <MenuItem key={f} value={f}>
                  {f.toUpperCase()}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <Button
            variant="contained"
            color="primary"
            sx={{ minWidth: 120, height: 56 }}
            onClick={fetchData}
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} /> : "Submit"}
          </Button>
        </Box>
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}
        <Box sx={{ display: "flex", justifyContent: "flex-end", mb: 1 }}>
          <Button
            variant="outlined"
            color="secondary"
            onClick={handleExport}
            disabled={(!rows.length && !hedgingRows.length) || !exportFormat}
          >
            Export {exportFormat ? exportFormat.toUpperCase() : ""}
          </Button>
        </Box>
        {/* Pricing Results Table */}
        <TableContainer component={Paper} sx={{ borderRadius: 2, mb: 3 }}>
          <Typography variant="h6" sx={{ mb: 1 }}>
            Pricing Result
          </Typography>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Symbol</TableCell>
                <TableCell align="right">Price</TableCell>
                <TableCell>Model</TableCell>
                <TableCell>Timestamp</TableCell>
                <TableCell align="right">Delta</TableCell>
                <TableCell align="right">Gamma</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((row, idx) => (
                <TableRow key={idx}>
                  <TableCell>{row.symbol}</TableCell>
                  <TableCell align="right">{row.price?.toFixed(2)}</TableCell>
                  <TableCell>{row.model}</TableCell>
                  <TableCell>{row.timestamp}</TableCell>
                  <TableCell align="right">{row.delta}</TableCell>
                  <TableCell align="right">{row.gamma}</TableCell>
                </TableRow>
              ))}
              {!rows.length && !loading && (
                <TableRow>
                  <TableCell colSpan={6} align="center">
                    No data to display.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
        {/* Hedging Actions Table */}
        <TableContainer component={Paper} sx={{ borderRadius: 2 }}>
          <Typography variant="h6" sx={{ mb: 1 }}>
            Hedging Actions
          </Typography>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Symbol</TableCell>
                <TableCell align="right">Price</TableCell>
                <TableCell>Model</TableCell>
                <TableCell>Action</TableCell>
                <TableCell>Timestamp</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {hedgingRows.map((row, idx) => (
                <TableRow key={idx}>
                  <TableCell>{row.symbol}</TableCell>
                  <TableCell align="right">{row.price?.toFixed(2)}</TableCell>
                  <TableCell>{row.model}</TableCell>
                  <TableCell>
                    <span
                      style={{
                        color:
                          row.action === "BUY STOCK"
                            ? "#388e3c"
                            : row.action === "SELL STOCK"
                            ? "#d32f2f"
                            : "#fbc02d",
                        fontWeight: 600,
                      }}
                    >
                      {row.action}
                    </span>
                  </TableCell>
                  <TableCell>{row.timestamp}</TableCell>
                </TableRow>
              ))}
              {!hedgingRows.length && !loading && (
                <TableRow>
                  <TableCell colSpan={5} align="center">
                    No data to display.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Container>
  );
}
