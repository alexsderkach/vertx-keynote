# Vert.x – The problem of real-time data binding
### Code from demo at JEEConf 2016

As the popularity of any event-driven application increases, the number of concurrent connections may increase. Applications that employ thread-per-client architecture, frustrate scalability by exhausting a server’s memory with excessive allocations and by exhausting a server’s CPU with excessive context-switching. One of obvious solutions, is exorcising blocking operations from such applications. Vert.x is event driven and non blocking toolkit, which may help you to achive this goal. In this talk, I covered it’s core features and developed a primitive application using WebSockets, RxJava and Vert.x.
