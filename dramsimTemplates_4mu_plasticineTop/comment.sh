echo "change init rand"
sed -i '/  this->__srand(rand_init);/r dramRandInit.txt' MultiMemoryUnitTester.cpp
sed -i '/  this->__srand(rand_init);/s/^/\/\//' MultiMemoryUnitTester.cpp
echo "finished changing init rand"
echo "start adding init declarations for DRAMSimulator outputs"
sed -i '/void MultiMemoryUnitTester_t::init */r dramInit.txt' MultiMemoryUnitTester.cpp
echo "init-ed"
echo "start modifying line connections going out of DRAMSimulator module"
sed  -i  '/  { val_t __r = this->__rand_val(); MultiMemoryUnitTester_DRAMSimulator_[1-3]__io_\(vldOut\|tagOut\|rdyOut\).values\[0\] = __r;}\|  { val_t __r = this->__rand_val(); MultiMemoryUnitTester_DRAMSimulator_[1-3]__io_rdata_[0-9]*.values\[0\] = __r;}\|  { val_t __r = this->__rand_val(); MultiMemoryUnitTester_DRAMSimulator__io_\(vldOut\|tagOut\|rdyOut\).values\[0\] = __r;}\|  { val_t __r = this->__rand_val(); MultiMemoryUnitTester_DRAMSimulator__io_rdata_[0-9]*.values\[0\] = __r;}/s/^/\/\//' MultiMemoryUnitTester.cpp
echo "line connections rerouted"
