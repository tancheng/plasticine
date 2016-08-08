#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include "rmapats.h"

scalar dummyScalar;
scalar fScalarIsForced=0;
scalar fScalarIsReleased=0;
scalar fScalarHasChanged=0;
scalar fForceFromNonRoot=0;
void  hsG_0(struct dummyq_struct * I972, EBLK  * I973, U  I703);
void  hsG_0(struct dummyq_struct * I972, EBLK  * I973, U  I703)
{
    U  I1179;
    U  I1180;
    U  I1181;
    struct futq * I1182;
    I1179 = ((U )vcs_clocks) + I703;
    I1181 = I1179 & 0xfff;
    I973->I636 = (EBLK  *)(-1);
    I973->I640 = I1179;
    if (I1179 < (U )vcs_clocks) {
        I1180 = ((U  *)&vcs_clocks)[1];
        sched_millenium(I972, I973, I1180 + 1, I1179);
    }
    else if ((peblkFutQ1Head != ((void *)0)) && (I703 == 1)) {
        I973->I641 = (struct eblk *)peblkFutQ1Tail;
        peblkFutQ1Tail->I636 = I973;
        peblkFutQ1Tail = I973;
    }
    else if ((I1182 = I972->I938[I1181].I653)) {
        I973->I641 = (struct eblk *)I1182->I652;
        I1182->I652->I636 = (RP )I973;
        I1182->I652 = (RmaEblk  *)I973;
    }
    else {
        sched_hsopt(I972, I973, I1179);
    }
}
#ifdef __cplusplus
extern "C" {
#endif
void SinitHsimPats(void);
#ifdef __cplusplus
}
#endif
