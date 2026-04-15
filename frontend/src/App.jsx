import React, { useMemo, useState } from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  ResponsiveContainer,
  CartesianGrid,
  BarChart,
  Bar
} from 'recharts';
import { fetchAnalytics, setAuthToken } from './services/api';
import StatsCard from './components/StatsCard';

const today = new Date();
const twoWeeksAgo = new Date(today);
twoWeeksAgo.setDate(today.getDate() - 14);

const fmt = (date) => date.toISOString().slice(0, 10);

function App() {
  const [hotelId, setHotelId] = useState(1);
  const [token, setToken] = useState('');
  const [startDate, setStartDate] = useState(fmt(twoWeeksAgo));
  const [endDate, setEndDate] = useState(fmt(today));
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const chartData = useMemo(() => {
    if (!data?.trend) return [];
    return data.trend.map((point) => ({
      date: point.businessDate,
      revenue: Number(point.totalRevenue),
      occupancy: Number(point.occupancyRate) * 100,
      revPar: Number(point.revPar)
    }));
  }, [data]);

  const loadAnalytics = async () => {
    setLoading(true);
    setError('');
    try {
      setAuthToken(token);
      const analytics = await fetchAnalytics(hotelId, startDate, endDate);
      setData(analytics);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch analytics.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page">
      <header>
        <h1>StayWise Revenue Command Center</h1>
        <p>Monitor occupancy and optimize hotel room pricing in real time.</p>
      </header>

      <section className="filters">
        <input
          placeholder="JWT Token"
          value={token}
          onChange={(e) => setToken(e.target.value)}
        />
        <input
          type="number"
          min="1"
          value={hotelId}
          onChange={(e) => setHotelId(Number(e.target.value))}
        />
        <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
        <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
        <button onClick={loadAnalytics} disabled={loading}>
          {loading ? 'Loading...' : 'Load Analytics'}
        </button>
      </section>

      {error ? <p className="error">{error}</p> : null}

      {data ? (
        <>
          <section className="cards">
            <StatsCard title="Total Revenue" value={`INR ${Number(data.totalRevenue).toLocaleString()}`} helper="Selected date range" />
            <StatsCard title="Average Occupancy" value={`${(Number(data.averageOccupancy) * 100).toFixed(1)}%`} helper="Across all loaded days" />
            <StatsCard title="Average RevPAR" value={`INR ${Number(data.averageRevPar).toLocaleString()}`} helper="Revenue per available room" />
          </section>

          <section className="grid">
            <div className="chartCard">
              <h3>Revenue Trend</h3>
              <ResponsiveContainer width="100%" height={320}>
                <LineChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="date" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Line type="monotone" dataKey="revenue" stroke="#2455d6" strokeWidth={2} />
                  <Line type="monotone" dataKey="revPar" stroke="#0a9b74" strokeWidth={2} />
                </LineChart>
              </ResponsiveContainer>
            </div>

            <div className="chartCard">
              <h3>Occupancy %</h3>
              <ResponsiveContainer width="100%" height={320}>
                <BarChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="date" />
                  <YAxis domain={[0, 100]} />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="occupancy" fill="#ef6a2e" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </section>
        </>
      ) : null}
    </div>
  );
}

export default App;
