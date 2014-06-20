ingress-speedwave
=================

Keeps track of your movement while playing Ingress and tells you when your actions would be blocked by the server due to the speed limit.

Motivation
----------

When travelling at high speeds, Ingress appears to block all interaction except picking up XM, which is particularly annoying when travelling
long distances by car or public transport.

The first theories in the Ingress community on how this could possibly work revolved around the concept of a "wave" that is triggered whenever you perform actions.
This wave would travel at a specific speed and whenever you moved faster than it, your actions would be blocked. Some further research revealed
that the actual speed limit was around 30mph, that each individual wave would not persist exactly 10 minutes and that Ingress would stop tracking your movement
once the activity is no longer active. A simple solution to "playing while travelling" is therefore setting a timer of 10 minutes and closing ingress - when that time has passed,
you can perform actions again, assuming that you are not moving at exceedingly high speeds. This solution is not very satisfying, though, so I was looking for an alternative.

Ingress Integrated Timer inspired me to write an application that would keep track of your movement while playing Ingress and be able to tell you when exactly you can play again -
i.e., when 10 minutes since the last location update in Ingress have passed or the speed wave passed by. 

Acknowledgements
----------------

keibertz
	Local agent empirically confirming the speed limit of 30 mph