package com.mygdx.mongojocs.snaiky;

public class ring
{
    ring prev;
    ring next;
    int y;
    int x;
    
    ring(ring _prev, ring _next,int _x, int _y)
    {
        prev = _prev;
        next = _next;
        x = _x;
        y = _y;
    }
}

