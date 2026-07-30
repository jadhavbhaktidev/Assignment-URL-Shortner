import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

interface ShortenResponse {
  id: number;
  shortUrl: string;
  alias: string;
}

interface MetricsResponse {
  urlId: number;
  clicks: number;
  uniques: number;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  longUrl = '';
  customAlias = '';
  shortUrl = '';
  alias = '';
  urlId: number | null = null;
  apiKey = 'change-me';
  isLoading = false;
  error = '';
  metrics: MetricsResponse | null = null;
  metricsError = '';
  deleteMessage = '';
  lookupUrlId = '';

  constructor(private http: HttpClient) {}

  shortenUrl() {
    this.error = '';
    this.shortUrl = '';
    this.alias = '';
    this.metrics = null;
    this.deleteMessage = '';
    this.isLoading = true;

    const payload = {
      longUrl: this.longUrl,
      customAlias: this.customAlias || undefined
    };

    this.http.post<ShortenResponse>('http://localhost:8080/api/v1/shorten', payload).subscribe({
      next: (response) => {
        this.shortUrl = response.shortUrl;
        this.alias = response.alias;
        this.urlId = response.id;
        this.lookupUrlId = response.id.toString();
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Unable to create a short URL. Please verify the backend is running.';
        this.isLoading = false;
      }
    });
  }

  fetchMetrics() {
    this.metricsError = '';
    this.metrics = null;
    if (!this.lookupUrlId) {
      this.metricsError = 'Enter a URL ID to fetch metrics.';
      return;
    }

    this.http.get<MetricsResponse>(`http://localhost:8080/api/v1/urls/${this.lookupUrlId}/metrics`, {
      headers: {
        'X-API-KEY': this.apiKey
      }
    }).subscribe({
      next: (response) => {
        this.metrics = response;
      },
      error: () => {
        this.metricsError = 'Unable to fetch metrics. Check the URL ID and API key.';
      }
    });
  }

  deleteUrl() {
    this.deleteMessage = '';
    if (!this.lookupUrlId) {
      this.deleteMessage = 'Enter a URL ID to delete.';
      return;
    }

    this.http.delete(`http://localhost:8080/api/v1/urls/${this.lookupUrlId}`, {
      headers: {
        'X-API-KEY': this.apiKey
      },
      observe: 'response'
    }).subscribe({
      next: (response) => {
        if (response.status === 204) {
          this.deleteMessage = 'URL deleted successfully.';
          if (this.urlId === Number(this.lookupUrlId)) {
            this.shortUrl = '';
            this.alias = '';
            this.urlId = null;
            this.metrics = null;
          }
        } else {
          this.deleteMessage = `Delete returned status ${response.status}.`;
        }
      },
      error: () => {
        this.deleteMessage = 'Unable to delete URL. Check the ID and API key.';
      }
    });
  }
}
