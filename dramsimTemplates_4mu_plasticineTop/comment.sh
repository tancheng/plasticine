echo "change init rand"
sed -i '/  this->__srand(rand_init);/r dramRandInit.txt' PlasticineTop.cpp
sed -i '/  this->__srand(rand_init);/s/^/\/\//' PlasticineTop.cpp
echo "finished changing init rand"
echo "start adding init declarations for DRAMSimulator outputs"
sed -i '/void PlasticineTop_t::init */r dramInit.txt' PlasticineTop.cpp
echo "init-ed"
echo "start modifying line connections going out of DRAMSimulator module"
sed  -i  '/  { val_t __r = this->__rand_val(); PlasticineTop_DRAMSimulator_[1-3]__io_\(vldOut\|tagOut\|rdyOut\).values\[0\] = __r;}\|  { val_t __r = this->__rand_val(); PlasticineTop_DRAMSimulator_[1-3]__io_rdata_[0-9]*.values\[0\] = __r;}\|  { val_t __r = this->__rand_val(); PlasticineTop_DRAMSimulator__io_\(vldOut\|tagOut\|rdyOut\).values\[0\] = __r;}\|  { val_t __r = this->__rand_val(); PlasticineTop_DRAMSimulator__io_rdata_[0-9]*.values\[0\] = __r;}/s/^/\/\//' PlasticineTop.cpp
echo "line connections rerouted"
