import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { PaymentService } from '../services/payment.service';
import { AuthService } from '../auth.service';
import { loadStripe } from '@stripe/stripe-js'; 

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent implements OnInit {
  paymentUrl: string = ''; 
  paymentSuccess: boolean = false; 

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private paymentService: PaymentService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const userId = this.authService.userId();

    if (userId !== null && userId !== undefined) {
      this.paymentService.createCheckoutSession(userId).subscribe(
        (response: any) => {
          console.log('Payment URL:', response.url);
          this.paymentUrl = response.url;
          window.location.href = this.paymentUrl;
        },
        (error: HttpErrorResponse) => {
          console.error('Payment error:', error);
        }
      );
    } else {
      console.error('User ID is missing.');
    }

    const sessionId = this.route.snapshot.queryParamMap.get('sessionId');
    if (sessionId) {
      this.paymentService.verifyPayment(sessionId).subscribe(
        (response: any) => {
          console.log('Payment verification response:', response);
          if (response) {
            this.paymentSuccess = true;
            this.router.navigate(['/payment-success']);
          } else {
            this.paymentSuccess = false;
          }
        },
        (error: HttpErrorResponse) => {
          console.error('Error verifying payment:', error);
          this.paymentSuccess = false; 
        }
      );
    }
  }
}
